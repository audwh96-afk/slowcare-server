# 1) Build stage: 소스에서 JAR 빌드
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
# 의존성 캐시
RUN mvn -B -ntp -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -ntp -DskipTests clean package

# 2) Runtime stage: 경량 JRE로 실행
FROM eclipse-temurin:17-jre
COPY --from=build /workspace/target/*.jar /app.jar
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java","-Dserver.port=8080","-jar","/app.jar"]