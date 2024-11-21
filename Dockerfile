# Etapa 1: Construcción
FROM maven:3.8.8-openjdk-17-slim AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos de proyecto al contenedor
COPY . .

# Da permisos de ejecución al script mvnw
RUN chmod +x mvnw

# Compila el proyecto y empaqueta la aplicación, omitiendo las pruebas
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen mínima para ejecución
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=builder /app/target/rentyourproperty-0.0.1-SNAPSHOT.jar .

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "rentyourproperty-0.0.1-SNAPSHOT.jar"]
