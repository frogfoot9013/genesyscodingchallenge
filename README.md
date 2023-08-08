# Readme for coding challenge.

Note: Dockerfile is now working in this version, using docker-compose.

## Information:
This project was built using Eclipse, on Manjaro Linux.
Versions used:
- OpenJDK 20.0.2
- Chromedriver 115.0.5790.110 (though quite likely other versions will work)
- Maven 3.8.7

## How to Run Docker Image
```console
docker-compose build
docker-compose up --abort-on-container-exit --exit-code-from acceptance_test 
```

## Note:
If running the test repeatedly in quick succession, the seats booked from the first instance will remain taken on Ryanair's side for a number of minutes before being freed up again. If one does not wait a few minutes between running, this will result in it failing on the second running when it tries to book these seats again.