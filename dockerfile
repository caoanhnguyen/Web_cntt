# Sử dụng OpenJDK 21 để build ứng dụng
FROM openjdk:21-jdk-slim AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Cấp quyền thực thi cho Maven Wrapper (nếu có)
RUN chmod +x mvnw

# Build project bằng Maven (bỏ qua test để build nhanh hơn)
RUN ./mvnw clean package -DskipTests

# Tạo container runtime chỉ chứa file JAR
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file JAR từ giai đoạn build sang container runtime
COPY --from=build /app/target/web_cntt-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8080 (hoặc cổng bạn dùng)
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
