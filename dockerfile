# Sử dụng OpenJDK 21
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file JAR từ target/ vào container
COPY target/*.jar app.jar

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
