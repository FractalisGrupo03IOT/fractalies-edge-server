# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el archivo `pom.xml` y el código fuente al contenedor
COPY pom.xml .
COPY src ./src

# Compilar el proyecto y construir el archivo JAR dentro del contenedor
RUN ./mvnw clean package -DskipTests

# Mover el archivo JAR a `app.jar`
RUN mv target/*.jar app.jar

# Exponer el puerto que usa la aplicación (30343 según tu configuración)
EXPOSE 30343

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
