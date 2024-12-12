# Sử dụng một image JDK chính thức
FROM openjdk:17-jdk-slim

# Sao chép file JAR từ target vào container
COPY target/Medical_appointment_booking_app.jar Medical_appointment_booking_app.jar

# Lệnh để chạy ứng dụng khi container được khởi động
ENTRYPOINT ["java", "-jar", "Medical_appointment_booking_app.jar"]