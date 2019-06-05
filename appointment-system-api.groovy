podTemplate(
        label: "maven-agent-rhel",
        cloud: "openshift",
        inheritFrom: "maven",
        containers: [
                containerTemplate(
                        name: "jnlp",
                        image: "docker-registry.default.svc:5000/cicd/jenkins-agent-maven-35-rhel7",
                        resourceRequestMemory: "1Gi",
                        resourceLimitMemory: "2Gi",
                        envVars: [
                                envVar(key: 'http_proxy', value: 'http://172.23.29.156:3128'),
                                envVar(key: 'https_proxy', value: 'http://172.23.29.156:3128'),
                                envVar(key: 'no_proxy', value: '.cluster.local,.csa.internal,.libgbl.biz,.svc,10.170.0.1,169.254.169.254,172.23.48.137,172.23.48.142,172.23.48.147,lg-l-n-msa00001,lg-l-n-msa00002,lg-l-n-msa00003,lg-l-n-msa00004,lg-l-n-msa00005,lg-l-n-msa00006,lg-l-n-msa00007,lg-l-n-msa00008,lg-l-n-msa00009,lg-l-n-msa00010,lg-l-n-msa00011,lg-l-n-msa00012,master-int.npd.msa.libgbl.biz')]
                )
        ]
)

        {
            node('maven-agent-rhel') {

                // Checkout Source Code
                stage('Checkout Source') {
                    //git credentialsId: 'a79aa448-2f04-4a85-b752-64a1721d3faa',
                    git branch: 'hystrix-stream', url: 'http://gogs.cicd.svc.cluster.local:3000/NL/appointment-servicecruiser-system-api.git'
                }

                // Define Maven Command. Make sure it points to the correct settings for our Nexus installation
                // The file nexus_settings.xml needs to be in the Source Code repository.
                def mvnCmd = "mvn -s settings/nexus-settings-lgi-msa-nonprod.xml"
                echo "mvnCmd: ${mvnCmd}"

                dir('.') {

                    // The following variables need to be defined at the top level
                    // and not inside the scope of a stage - otherwise they would not be accessible from other stages.
                    // Extract version and other properties from the pom.xml
                    def groupId    = getGroupIdFromPom("pom.xml")
                    def artifactId = getArtifactIdFromPom("pom.xml")
                    def version    = getVersionFromPom("pom.xml")


                    // Set the tag for the production image: version -- to be discussed
                    def prodTag = "${version}"
                    echo "prodTag: ${prodTag}"
                    GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
                    GIT_COMMIT_HASH = GIT_COMMIT_HASH.toString().substring(0,7)
                    def commitId = "${GIT_COMMIT_HASH}"
                    echo "commitId: ${commitId}"
                    if (getVersionFromPom("pom.xml").toString().toUpperCase().contains("SNAPSHOT")) {

                        version = getVersionFromPom("pom.xml").toString().toUpperCase().replace("SNAPSHOT", "")

                    } else {
                        //If the condition is false print the following statement
                        version = getVersionFromPom("pom.xml").toString();
                        println("versionInfo without snapshot extension :" +version);
                    }

                    echo "the version is :  ${version}"

                    // Set the tag for the development image: version + build number
                    def devTag  = "${version}-${commitId}"
                    echo "devTag: ${devTag}"

                    // Using Maven build the war file
                    // Do not run tests in this step
                    stage('Build jar') {
                        echo "Building version ${devTag}"

                        //sh "${mvnCmd} clean install package -DskipTests"
                        sh "${mvnCmd} clean package -DskipTests=true"

                    }

                    stage('Create Image Builder') {

                        echo "Creating BuildConfig"

                        sh "if oc get bc appointment-system-api --namespace=nl-appointment-dev; \
                            then echo \"exist\"; \
                            else oc new-build --binary=true --name=appointment-system-api redhat-openjdk18-openshift:1.4 --labels=app=appointment-system-api -n nl-appointment-dev;fi"

                      }

                    // Build the OpenShift Image in OpenShift and tag it.
                    stage('Build and Tag OpenShift Image') {
                        echo "Building OpenShift container image appointment-system-api:${devTag}"
                        sh "oc start-build appointment-system-api --follow --from-file=target/${artifactId}.jar -n nl-appointment-dev"

                        // Tag the image using the devTag
                        openshiftTag alias: 'false', destStream: 'appointment-system-api', destTag: devTag, destinationNamespace: 'nl-appointment-dev', namespace: 'nl-appointment-dev', srcStream: 'appointment-system-api', srcTag: 'latest', verbose: 'false'
                    }

                    stage('Create configMap') {

                        echo "Creating ConfigMap"

                        sh "if oc get cm appointment-system-api --namespace=nl-appointment-dev; \
                            then oc delete cm appointment-system-api --namespace=nl-appointment-dev \
                            && oc create configmap appointment-system-api --from-file=./src/main/resources/application.yml -n nl-appointment-dev; \
                            else oc create configmap appointment-system-api --from-file=./src/main/resources/application.yml -n nl-appointment-dev;fi"

                        echo "Label ConfigMap"

                        sh "oc label configmap/appointment-system-api app=appointment-system-api -n nl-appointment-dev"
                    }

                    stage('Create Deployment') {

                        echo "Creating DeployConfig"

                        sh "if oc get dc appointment-system-api --namespace=nl-appointment-dev; \
                            then echo \"exist\"; \
                            else oc new-app nl-appointment-dev/appointment-system-api:${devTag} \
                            --name=appointment-system-api --allow-missing-imagestream-tags=true \
                            --labels=app=appointment-system-api -n nl-appointment-dev \
                            && oc set probe dc/appointment-system-api -n nl-appointment-dev --liveness --failure-threshold 15 --initial-delay-seconds 30 -- echo ok \
                            && oc set probe dc/appointment-system-api --readiness --failure-threshold 15 --initial-delay-seconds 60 --get-url=http://:8080/actuator/health -n nl-appointment-dev;fi"

                        echo "Expose the service"

                        sh "if oc get svc appointment-system-api --namespace=nl-appointment-dev; \
                            then echo \"exist\"; \
                            else oc expose dc appointment-system-api --port 8080 -n nl-appointment-dev \
                            --labels=app=appointment-system-api \
                            && oc create route edge --service=appointment-system-api -n nl-appointment-dev;fi"

                    }

                    // Load configMap as a volume
                    stage('Load configMap as a volume') {

                        echo "Load ConfigMap as volume"

                        sh "oc set volumes dc/appointment-system-api --add --overwrite=true \
                        --name=config-volume --mount-path=/deployments/config -t configmap \
                        --configmap-name=appointment-system-api -n nl-appointment-dev"

                    }

                    // Create SslKey secret .
                    stage('Create secret') {

                        echo "Creating SslKey Secret"

                        sh "if oc get secret lgi-msp-ssl-cert --namespace=nl-appointment-dev; \
                            then echo \"lgi-msp-ssl-cert allready exist\"; \
                            else oc create secret generic lgi-msp-ssl-cert --from-file=./src/main/resources/keys/cacerts_lgi.jks -n nl-appointment-dev;fi"

                    }

                    // Load secret as a volume
                    stage('Load secret as a volume') {

                        echo "Load Secret as volume"

                        sh "oc set volumes dc/appointment-system-api --add --overwrite=true \
                        --name=secret-volume --mount-path=/etc/config/keys -t secret \
                        --secret-name=lgi-msp-ssl-cert -n nl-appointment-dev"

                    }

                    stage('Set Environment variables') {


                        sh "oc set env dc/appointment-system-api JAVA_OPTIONS=-Dspring.profiles.active=openshift-dev -n nl-appointment-dev"

                    }

                    // Deploy the built image to the Development Environment.
                    stage('Deploy to Dev') {
                        echo "Deploying container image to Development Project"

                        // Update the Image on the Development Deployment Config
                        sh "oc set image dc/appointment-system-api appointment-system-api=docker-registry.default.svc:5000/nl-appointment-dev/appointment-system-api:${devTag} -n nl-appointment-dev"

                        // Deploy the development application.
                        openshiftDeploy depCfg: 'appointment-system-api', namespace: 'nl-appointment-dev', verbose: 'false', waitTime: '20', waitUnit: 'min'
                        openshiftVerifyDeployment depCfg: 'appointment-system-api', namespace: 'nl-appointment-dev', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false', waitTime: '', waitUnit: 'sec'
                        openshiftVerifyService namespace: 'nl-appointment-dev', svcName: 'appointment-system-api', verbose: 'false'
                    }
                }
            }
        }
        def getVersionFromPom(pom) {
    def matcher = readFile(pom) =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}
def getGroupIdFromPom(pom) {
    def matcher = readFile(pom) =~ '<groupId>(.+)</groupId>'
    matcher ? matcher[0][1] : null
}
def getArtifactIdFromPom(pom) {
    def matcher = readFile(pom) =~ '<artifactId>(.+)</artifactId>'
    matcher ? matcher[0][1] : null
}

