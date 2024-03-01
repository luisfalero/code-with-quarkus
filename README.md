# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Example Deploy

Descargar ejemplo de: https://code.quarkus.redhat.com/

### Local Deploy

- Opción 01:

```shell
mvn package -Dnative
```

- Opción 02:

```shell
podman build -f src/main/docker/Dockerfile.multistage -t code-with-quarkus:1.0.0 .
```

```shell
podman run --rm --name code-with-quarkus -p 8080:8080 code-with-quarkus:1.0.0
```

### OCP Registry

```shell
REGISTRY="default-route-openshift-image-registry.apps.cluster-h6vq5.dynamic.redhatworkshops.io"
API_OCP="api.cluster-h6vq5.dynamic.redhatworkshops.io:6443"
```

```shell
oc login -u ${USER} -p ${PASSWORD} ${API_OCP}
```

```shell
podman login -u $(oc whoami) -p $(oc whoami -t) ${REGISTRY}
```

```shell
podman push code-with-quarkus:1.0.0 ${REGISTRY}/code-with-quarkus:1.0.0
```

### Quay Registry

```shell
REGISTRY="quay.io"
```

```shell
podman login -u rh_ee_lfalero ${REGISTRY}
```

```shell
podman push code-with-quarkus:1.0.0 ${REGISTRY}/code-with-quarkus:1.0.0
```

### OCP Deploy

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
          image: image-registry.openshift-image-registry.svc:5000/redhat-test/code-with-quarkus:1.0.0
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

```shell
oc  -n redhat-test create route edge --service code-with-quarkus --port 8080
```