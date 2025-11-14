# ============================
# 1. Build Stage (Maven)
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar solo pom.xml primero (cache inteligente)
COPY pom.xml .

# Descargar dependencias para acelerar builds posteriores
RUN mvn -q dependency:go-offline

# Copiar el código fuente
COPY src ./src

# Empaquetar
RUN mvn clean package -DskipTests


# ============================
# 2. Runtime Stage (JRE liviano)
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar el JAR desde el build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto interno del ApiGateway
EXPOSE 8080

# Optimización recomendada para Spring Boot en contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
