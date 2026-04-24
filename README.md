# Hexagonal architecture messenger
Backend API service built using hexagonal architecture and implemented with Kotlin's ktor.

# Overview

## What tools/frameworks do we use?

- **gradle** - our build system of choice (using Kotlin DSL)
- **kotlin** - our language of choice
- **ktor 2** for creating web application: https://github.com/ktorio/ktor
- **ExposedSQL** to access database: https://github.com/JetBrains/Exposed
- **HikariCP** for high-performance JDBC connection pool: https://github.com/brettwooldridge/HikariCP
- **Koin** for dependency injection: https://insert-koin.io/
- **PostgreSQL** for database: https://www.postgresql.org/
- **HOCON** for application configuration: https://github.com/lightbend/config/
- **OpenAPI** to generate REST data models

and for testing:
- **Testcontainers** for testing with a real database in Docker: https://github.com/testcontainers/testcontainers-java
- **kotest** for writing tests: https://kotest.io

and some other misc stuff:
- **ktlint** for Kotlin checkstyle (https://pinterest.github.io/ktlint/)
- **kover** for code coverage metrics (https://github.com/Kotlin/kotlinx-kover)

# Setup for run

## Prerequisites

#### Docker

Docker not required to run this code locally, but makes setup a little easier. We recommend installing it.

#### PostgreSQL

If you have a local PostgreSQL running on your computer - you are all set.
If you don't have PostgreSQL installed, you can do it now by installing from:
https://www.postgresql.org/download/
and set it up with user name and password that can later be used to configure.

Another option is to create a docker image with PostgreSQL database running:

```bash
$ docker run --name localpostgres -d -p 5432:5432 -e POSTGRES_PASSWORD=postgrespass postgres:alpine
```

This will create and run a local instance of PostgreSQL database with user name "postgres" and password "postgrespass".

Or use

```bash
$ docker start localpostgres
```

if localpostgres container was already created.

Make sure to create **addrbook** database in your PostgreSQL instance.

## Run application locally

#### Run application with gradle

Application uses HOCON configuration files and some parameters rely on environment variable,
so you need to set them up first:

APP_DEPLOYMENT_ENV=local;APP_DB_USERNAME=baeldung;APP_DB_PASSWORD=baeldung;APP_DB_URI=jdbc:postgresql://localhost:5432/ktor-messenger;APP_VERSION=0.0;APP_BUILD_NUMBER=0

- Deployment target. Application can have multiple configurations (local, dev, sit, prod etc) and therefore
  multiple resource files, such as `infra/src/main/resources/config-local.conf` (`config-prod.conf` etc) are available.
  Depending on a target specified in APP_DEPLOYMENT_ENV environment variable - different configuration files
  will be selected

        $ export APP_DEPLOYMENT_ENV=local

- Application version:

        $ export APP_VERSION=0.1

- Application build number:

        $ export APP_BUILD_NUMBER=1

- PostgreSQL database username, password and connection URI:

        $ export APP_DB_USERNAME=postgres
        $ export APP_DB_PASSWORD=postgrespass
        $ export APP_DB_URI=jdbc:postgresql://localhost:5432/addrbook

Next step is to run application via gradle:

        ./gradlew build :app:infra:run --args='-config=src/main/resources/application-dev.conf'

If you don't specify --args argument, then by default application.conf will be loaded. Both files are very similar,
but application-dev.conf could contain some settings helpful during a development phase (e.g. auto-reload support).

Application exposes HTTP 8080 port for its API.

#### Run application with IntelliJ

Import ktor-hexagonal-multimodule project into your IntelliJ IDE. Choose **Run / Edit Configuration** menu to create new
launch configuration. On the left side click **[+]** and select **Application**. Name it **API Server** (or pick other name)
and fill up some essential fields:

- Module: ktor-hexagonal-multimodule.app.infra.main
- Main class: `io.ktor.server.netty.EngineMain`
- Program arguments: `-config=app/infra/src/main/resources/application-dev.conf`
- Environment variables: `APP_DEPLOYMENT_ENV=local;APP_DB_USERNAME=postgres;APP_DB_PASSWORD=my_password_123;APP_DB_URI=jdbc:postgresql://localhost:5432/ktor-messenger;APP_VERSION=0.1;APP_BUILD_NUMBER=1;APP_JWT_ACCESS_SECRET=secret;APP_JWT_REFRESH_SECRET=secred`

You should be able to run/debug your app now.

#### Run with Docker locally

Build an application first:

        $ ./gradlew build shadowJar

This step will build fat jar at `app/infra/build/libs/infra-all.jar`

Then upload and tag it in docker:

        $ docker build -t addrbook-api-server . 

Now you are ready to lunch it:
```
$ docker run -it \ 
    -e APP_DEPLOYMENT_ENV=local \
    -e APP_VERSION=0.1 \
    -e APP_BUILD_NUMBER=1 \
    -e APP_DB_USERNAME=postgres \
    -e APP_DB_PASSWORD=postgrespass \
    -e APP_DB_URI=jdbc:postgresql://your-local-ip-address:5432/addrbook \
    -p 8080:8080 \
    --rm addrbook-api-server
```

Make sure to replace *your-local-ip-address* in APP_DB_URI in command above to the actual IP address of your machine
that you can find with **ifconfig** or **ipconfig** shell commands (you cannot use *localhost* anymore, because localhost
inside AddressBook application docker container will be pointing to that container instead of your host machine).


## HOCON configuration

Application configurations are stored in popular and powerful HOCON format (https://github.com/lightbend/config).
Our config files location is at `app/infra/src/main/resource` and here you can find files such
as `config-common.conf`, `config-local.conf` and `config-prod.conf`. Common file is used to keep
all configurations that can be shared between all deployments, while `-local` and `-prod` means
that configuration will be loading for specific environments (you can add your own if needed).
This second part after dash `-` must match what you pass via `APP_DEPLOYMENT_ENV` environment
variable.


## REST data models

We use a hybrid approach to build REST endpoints in our ktor code. All ktor routes are created manually in
the code, while data models (payloads) for PUT/POST requests as well as all data model responses - declared
in OpenAPI spec. You can find them in `app/adapters/primary-web/src/main/resources/openapi/addrbook.yaml` file.
By using openapi generator plugin we generate kotlin data classes based on this file. If you decide to
add/change REST models, you can edit this file and run code generator after that:

        $ ./gradlew openApiGenerate


## Testing

### Unit tests

Unit tests automatically run when you perform a build. All tests are light-weight and use mocks, with the noticeable
exception of `app/adapters/persist` module. Here we use Test Containers to spin up docker image with PostgreSQL,
so tests for adapters are run with a real PostgreSQL database, no database mocks are used. This will allow us to
be more confident in our code that interacts with database.

Not that if you want to run unit tests per file or class level in IntelliJ, you must install Kotest plugin
from marketplace.

### Manual tests

There is an OpenAPI specification in `app/adapters/primary-web/src/main/resources/openapi/addrbook.yaml` file,
you can import it into Postman if you want to manually call app's REST APIs.


## Generate coverage report

To generate a coverage report in HTML format you must run:

    $ ./gradlew build koverHtmlReport

It will run unit tests and generate coverage report in HTML format into project's directory:
`./app/adapters/persist/build/reports/kover/merged/html/index.html`

There are other tasks (such as koverMergedXmlReport, etc. available).


## Logging

This application uses simple logging based on slf4j. Note that our code supports X-Request-Id HTTP header
that you can specify when perform HTTP request (or it will be auto-generated). The value of this header
will be attached to every log line (in form of `CallRequestId=...`) which will assist you in troubleshooting
your log files (or in Splunk).

## Helper functionality

### MustBeCalledInTransactionContext annotation

This annotation is used to mark functions that must be called in a transaction context. It is used
in `app/adapters/persist` module to make sure that all database calls are performed in a transaction
context. If you forget to wrap a function with this annotation in a transaction context - you will
get a compile time error. 
