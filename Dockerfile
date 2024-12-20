FROM bellsoft/liberica-openjdk-debian:23.0.1 as builder
#FROM bellsoft/liberica-runtime-container:jdk-23-stream-musl as builder
WORKDIR /
COPY ./gradle gradle
COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .
COPY ./gradlew .
RUN chmod +x ./gradlew
COPY ./src /src
# Build application and mount the Gradle cache located under /root/.gradle ($HOME/.gradle)
#RUN --mount=type=cache,target=/root/.gradle cd /src && /gradlew clean build
#RUN ./gradlew clean build
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon -i clean build

#FROM bellsoft/liberica-runtime-container:jre-23-stream-musl as runner
FROM bellsoft/liberica-openjre-debian:23.0.1 as runner
WORKDIR /
COPY --from=builder /build/libs/web-0.0.1-SNAPSHOT.jar .
ENV JAVA_OPTS="-Xmx512M -Xms512M"
EXPOSE 8080
CMD ["java", "-jar", "web-0.0.1-SNAPSHOT.jar"]
