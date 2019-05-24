## tracing
# Description
This is an E2E example on how to use spring boot jaeger tracing for distributed microservices which is using spring and camel in the app. 
#Usage
To use this example, 

Step 1:<br>
clone this branch

Step 2:<br>
start the [jaeger all-in-one docker image](https://www.jaegertracing.io/docs/1.11/getting-started/)

```
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.11
  ```
  
  Step 3:<br>
  Start the application (both trace-client & trace-server). This example is built on intellij and has not been tested for mvn commands to make it generic.
  
  Step 4:<br>
  Execute the following uri http://localhost:8079/trace-client with a GET method.
 
  Step 4: <br>
  Login to jaeger to see the trace infomation at http://localhost:16686/
  
  Enjoy!!!!!
