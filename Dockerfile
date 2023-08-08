FROM maven:3.8.7

WORKDIR /usr/src/app

COPY pom.xml .
COPY src/ ./src/

RUN mvn clean

CMD ["mvn", "test"]