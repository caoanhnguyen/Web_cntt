# Build Stage
FROM openjdk:21-jdk-slim AS build
WORKDIR /app

# Copy toàn bộ project
COPY . .

# Chỉ chạy chmod nếu có mvnw
RUN if [ -f mvnw ]; then chmod +x mvnw; fi

# Build với Maven Wrapper (hoặc dùng mvn nếu không có mvnw)
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy file JAR từ giai đoạn build
COPY --from=build /app/target/web_cntt-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
