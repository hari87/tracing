# spring configmaps and profiles

This example is inspired from https://dzone.com/articles/configuring-spring-boot-on-kubernetes-with-configm
Create a configmap with the following command that would load your application.yml file into openshift project. 
oc create configmap spring-app-config --from-file=src/main/resources/application.yml
then execute mvn fabric8:deploy

then you test the service by <hostname>/greeting?name=devoteam

In the deployment.yml the configuration is set to use profile of prod, hwoever you can create your own profile and update the profile name in deployment.yml
