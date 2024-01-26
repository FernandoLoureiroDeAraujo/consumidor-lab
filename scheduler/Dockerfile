FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /workspace/app

COPY scheduler/mvnw .
COPY scheduler/.mvn .mvn
COPY scheduler/pom.xml .
COPY scheduler/src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

COPY ../docker/agent/opentelemetry-javaagent.jar /workspace/opentelemetry-javaagent.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5002", "-javaagent:/workspace/opentelemetry-javaagent.jar","-cp","app:app/lib/*","br.com.flallaca.scheduler.SchedulerApplication"]