
FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew gradlew.bat gradle.properties settings.gradle build.gradle main.gradle ./
COPY lombok.config ./

COPY applications/ applications/
COPY domain/ domain/
COPY infrastructure/ infrastructure/

RUN chmod +x gradlew
RUN ./gradlew :app-service:bootJar -x test -x validateStructure --no-daemon --no-configuration-cache


FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp


RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser


COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar
RUN chown appuser:appuser app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
