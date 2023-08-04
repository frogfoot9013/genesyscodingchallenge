# Readme for coding challenge.

Note: Dockerfile is not working as intended - time constraints brought about by unforeseen events precluded me getting it to work, and as it stands, it runs into a SessionNotCreatedException when starting chrome and chromedriver in the docker container, when the container is run.

## Information:
This project was built using Eclipse, on Manjaro Linux.
Versions used:
- OpenJDK 20.0.2
- Chromedriver 115.0.5790.110 (though quite likely other versions will work)
- Maven 3.8.7

## How to Run
```console
mvn clean
mvn test
```