FROM maven:3.9.6 as builder
COPY . /authentication/app
WORKDIR /authentication/app
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /authentication/app
COPY --from=builder /authentication/app/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]