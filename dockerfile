# Sử dụng Maven 3.8 + JDK 21 để build ứng dụng
FROM maven:3.8.8-eclipse-temurin-21 AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Copy file .env vào container
COPY .env /app/.env

# Copy file JSON vào thư mục chính xác
COPY src/main/resources/webcnttkma-firebase-adminsdk-mpsu6-ac2c4a48ff.json /app/firebase-config.json

# Build project bằng Maven, bỏ qua test để build nhanh hơn
RUN mvn clean package -DskipTests

# Giai đoạn runtime - Chạy ứng dụng
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file JAR từ giai đoạn build
COPY --from=build /app/target/web_cntt-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8084
EXPOSE 8084

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
