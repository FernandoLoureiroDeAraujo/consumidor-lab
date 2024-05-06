FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

ARG DEPENDENCY=consumer/target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

COPY ../docker/agent/opentelemetry-javaagent.jar /workspace/opentelemetry-javaagent.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5002", "-javaagent:/workspace/opentelemetry-javaagent.jar","-cp","app:app/lib/*","br.com.flallaca.consumer.ConsumerApplication"]

#FROM eclipse-temurin:17-jdk-alpine as build
#
#WORKDIR /workspace/app
#
#COPY consumer/mvnw .
#COPY consumer/.mvn .mvn
#COPY consumer/pom.xml .
#
## Instala as dependências do Maven
#RUN ./mvnw -f pom.xml dependency:go-offline
#
#COPY consumer/src src
#
#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
#
#FROM eclipse-temurin:17-jdk-alpine
#
#VOLUME /tmp
#ARG DEPENDENCY=/workspace/app/target/dependency
#
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#
#COPY ../docker/agent/opentelemetry-javaagent.jar /workspace/opentelemetry-javaagent.jar
#
#EXPOSE 5005
#
#ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5002", "-javaagent:/workspace/opentelemetry-javaagent.jar","-cp","app:app/lib/*","br.com.flallaca.consumer.ConsumerApplication"]