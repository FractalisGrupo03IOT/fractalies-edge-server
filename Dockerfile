# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el archivo JAR generado en el contenedor Docker
COPY target/Fractalies-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa la aplicación (30343 según tu configuración)
EXPOSE 30343

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
