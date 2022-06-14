# kogibot Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running Kogitbot locally

Create e .env file on the project's root directory with the following environment variables:

```java
QUARKUS_GITHUB_APP_APP_ID=<the numeric app id>
        QUARKUS_GITHUB_APP_APP_NAME=<the name of your app>
        QUARKUS_GITHUB_APP_WEBHOOK_PROXY_URL=<your Smee.io channel URL>
        QUARKUS_GITHUB_APP_WEBHOOK_SECRET=<your webhook secret>
        QUARKUS_GITHUB_APP_PRIVATE_KEY=-----BEGIN RSA PRIVATE KEY-----\
<your private key>                          \
        -----END RSA PRIVATE KEY-----
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Code Style

This project uses a similar Java code style than https://github.com/kiegroup/droolsjbpm-build-bootstrap/tree/main/ide-configuration

TODO
 - add license header and configure maven plugin to check it
 - add maven checkstyle plugin based on the kiegroup java profile
 - 