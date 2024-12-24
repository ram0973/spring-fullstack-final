//import org.gradle.kotlin.dsl.resolver.DefaultKotlinBuildScriptModelRepository.processor

plugins {
    // https://plugins.gradle.org/
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	//id("org.hibernate.orm") version "6.6.3.Final"
	//id("org.graalvm.buildtools.native") version "0.10.2"
    //id("gg.jte.gradle") version "3.1.12"
}

group = "dev"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

//extra["springShellVersion"] = "3.3.1"

dependencies {
    // https://central.sonatype.com/
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframework.shell:spring-shell-starter")
    implementation("net.datafaker:datafaker:2.4.2")
    //implementation("org.liquibase:liquibase-core")

	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // mapstruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // jte
    //implementation("gg.jte:jte:3.1.12")
    //implementation("gg.jte:jte-spring-boot-starter-3:3.1.12")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	//developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")

	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

//jte {
//    generate()
//    binaryStaticContent = true
//}

dependencyManagement {
	imports {
        //mavenBom("org.springframework.shell:spring-shell-dependencies:${property("springShellVersion")}")
	}
}

//hibernate {
//	enhancement {
//		enableAssociationManagement = true
//	}
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
