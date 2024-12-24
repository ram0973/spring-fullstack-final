FROM bellsoft/liberica-openjdk-debian:23.0.1
COPY ./gradle gradle
COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .
COPY ./gradlew .
RUN chmod +x ./gradlew
COPY ./src /src
RUN ./gradlew --no-daemon build
ENV JAVA_OPTS="-Xmx512M -Xms512M"
EXPOSE 8080
CMD ["java", "-jar", "build/libs/web-0.0.1-SNAPSHOT.jar"]
