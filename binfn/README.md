# String to Binary Function

This is a kafka-binded SCSt function that converts a string to it's binary representation.

Example Input:

```
"Hello World"   
```

Would output:

```agsl
1001000110010111011001101100110111110000010101111101111111001011011001100100
```

## Configuration

This function can be configured using the following environment variables:

| Name                                                   | Description | Default      |
|--------------------------------------------------------|-------------|--------------|
| `spring.cloud.stream.function.definition`              | The name of the function | `binFn`      |   
| `spring.cloud.stream.bindings.binFn-in-0.destination`  | The name of the input topic | `strings`    |
| `spring.cloud.stream.bindings.binFn-out-0.destination` | The name of the output topic | `binstrings` |
 | `spring.cloud.stream.kafka.binder.brokers`             | The Kafka broker address | `localhost:9092` |


## Spring Native Support

This function should be complied as a native binary, which means when you deploy it to 
Tanzu Application Service, it will be compiled as a native binary. RuntimeHints are used
to ensure that the function is compiled with the correct dependencies.

## Building

To build the function, run the following command:

```bash
mvn clean -Pnative native:compile -DskipTests
```

Alternately, you may build a docker image with the command:

```bash
mvn clean -Pnative -DskipTests spring-boot:build-image
```

## Invocation

To run the function with parameters, for example to explicitly specify in/out destination, run the following command:

```bash
./target/binfn --spring.cloud.stream.bindings.binFn-in-0.destination=strings --spring.cloud.stream.bindings.binFn-out-0.destination=binstrings
```

However, run with the Docker compose provided in `infra/docker-compose.yml`. 
This will stand up a Kafka broker and a Zookeeper instance. Additionally, an HTTP 
source is provided to send messages to the function and a websocket sink are provided to test the function.

Once it's running, in a new terminal, listen to the output websocket:

```bash
websocat ws://localhost:9095/ws
```

Then, in another terminal, send a message to the function:

```bash
curl -X POST -H "Content-Type: text/plain" -d "Hello World" http://localhost:8091
```

Back to the websocat output, you should see ```1001000110010111011001101100110111110000010101111101111111001011011001100100```.


## Links

[Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream)

[Spring Cloud Stream Kafka Binder](https://cloud.spring.io/spring-cloud-stream-binder-kafka/spring-cloud-stream-binder-kafka.html)

[Using the Tanzu Java Native Image Buildpack](https://docs.vmware.com/en/VMware-Tanzu-Buildpacks/services/tanzu-buildpacks/GUID-java-native-image-java-native-image-buildpack.html)



