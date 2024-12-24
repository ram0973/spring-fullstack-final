# FullStack веб - приложение на Spring и React

Used tech stack:

- Java 21+ - Graal VM JDK (not supports Shenandoah GC) or others
(not OpenJDK, it's slowest.)
- Spring Boot 3.3+
- Docker
- Liquibase
- Hibernate ORM
- Postgresql
- Gradle Kotlin
- MapStruct
- OpenApi
- React
- TypeScript
- Bun
- Mantine
- PostCSS (with Mantine)

## Software

- Intellij Idea Pro/Community
- Docker
- Graal VM JDK/Some other JDK
- Git
- Bun

## Graal VM JDK
Open start.spring.io
Downloaded project template
[here](https://start.spring.io/#!type=gradle-project-kotlin&language=java&platformVersion=3.3.2&packaging=jar&jvmVersion=21&groupId=dev&artifactId=web&name=web&description=Web%20project%20with%20Spring%20Boot&packageName=dev.web&dependencies=native,devtools,lombok,configuration-processor,docker-compose,web,thymeleaf,security,oauth2-client,oauth2-resource-server,data-jpa,liquibase,postgresql,validation,mail,actuator,testcontainers,spring-shell)

Download Graal VM [here](https://github.com/graalvm/graalvm-ce-builds/releases) or other

[settings](https://www.graalvm.org/latest/docs/getting-started/windows/) :

Unpack to C:\lib\graavm21-0-2

Added path to executables:

setx /M PATH "C:\lib\graavm21-0-2\bin\;%PATH%"

Set JAVA_HOME:

setx /M JAVA_HOME "C:\lib\graavm21-0-2\"

Install Visual Studio. Setup as written above.

Check:
```bash
java --version
native-image.cmd --version
```

## Kotlin Gradle

In gradle/wrapper/gradle-wrapper.properties change version to yours:

[Versions list here](https://gradle.org/releases/)

distributionUrl=https\://services.gradle.org/distributions/gradle-8.8-bin.zip

For example, instead 8.9 - 8.10

OR (better)

make setup
git update-index --chmod=+x gradlew

## Idea

Set GraalVM or other JDK here : File - Project structure.

Help - Edit Custom VM Options:
-Xms2048m
-Xmx2048m
-XX:+UseShenandoahGC (not working with GraalVM)

Idea settings: search "encod"
File Encodings and Console: set UTF-8.

Set: Auto Imports - unambiguous imports on the fly.

Install plugins, as you wish:
- Mandatory - EnvFile, and in the run settings choose .env file
- JPA Buddy
- Rainbow Brackets


## Secondary

Created .editorconfig, api.http, compose.yml, gradle.properties.
Copy .env from .env.example.

## Libraries

(Got [here](https://mvnrepository.com/) with Kotlin Gradle
- OpenApi
- MapStruct

OpenApi (Swagger) opens here: /swagger-ui/index.html

## Setup Git

```bash
git config --global user.name "Your name"
git config --global user.email your_email
git config --global init.defaultBranch main
```

## Others
.gradle and build folders - you can delete them in case of troubles

Idea repair:  File - Repair Ide or: File - Invalidate Caches

## Docker & WSL
Install wsl: wsl --install Ubuntu # Docker like standard WSL
Only after wsl up and running install Docker!
