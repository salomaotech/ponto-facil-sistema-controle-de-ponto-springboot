FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Instala dependências (incluindo cliente MySQL)
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk mysql-server mysql-client && \
    apt-get clean

# Copia o script SQL
COPY init.sql /init.sql

# Copia a aplicação
COPY ponto-0.0.1-SNAPSHOT.jar /app.jar
COPY application.properties /application.properties

# Expõe as portas
EXPOSE 8080
EXPOSE 3306

# Inicia o MySQL, configura senha, cria banco e inicia o Spring Boot
CMD bash -c "\
  service mysql start && \
  sleep 30 && \
  mysql -e \"ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456'; FLUSH PRIVILEGES;\" && \
  mysql -u root -p123456 < /init.sql && \
  java -jar /app.jar"
