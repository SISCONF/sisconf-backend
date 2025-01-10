FROM eclipse-temurin:21-jdk-alpine
WORKDIR /workspace/app

# Copiar apenas os arquivos de dependência primeiro
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Baixar dependências
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte e buildar
COPY src src
RUN ./mvnw package -DskipTests

ENTRYPOINT ["java", "-jar", "app.jar"]