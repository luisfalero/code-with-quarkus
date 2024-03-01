# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Example

```shell script
quarkus create && cd code-with-quarkus
```

```shell script
mvn package -Dnative -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
```

```shell script
podman build -f src/main/docker/Dockerfile.multistage -t code-with-quarkus:1.0.0 .

podman run --rm --name code-with-quarkus -p 8080:8080 code-with-quarkus:1.0.0
```

```shell script
podman login -u rh_ee_lfalero quay.io
```

```shell script
podman push quay.io/rh_ee_lfalero/code-with-quarkus:1.0.0
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: code-with-quarkus
  namespace: redhat-test
  labels:
    app: code-with-quarkus
spec:
  replicas: 1
  selector:
    matchLabels:
      app: code-with-quarkus
  template:
    metadata:
      labels:
        app: code-with-quarkus
    spec:
      containers:
        - name: code-with-quarkus
          #image: quay.io/rh_ee_lfalero/code-with-quarkus:1.0.0
          image: image-registry.openshift-image-registry.svc:5000/redhat-test/code-with-quarkus:latest
          ports:
            - containerPort: 8080
```

```yaml
kind: Service
apiVersion: v1
metadata:
  name: code-with-quarkus
  namespace: redhat-test
  labels:
    app: code-with-quarkus
spec:
  ports:
    - name: 8080-tcp
      protocol: TCP
      port: 8080
      targetPort: 8080
  selector:
    app: code-with-quarkus
```

```shell script
oc create route edge --service code-with-quarkus --port 8080
```