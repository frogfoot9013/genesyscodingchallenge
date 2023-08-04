FROM maven:3.8.7

WORKDIR /usr/src/app

COPY pom.xml .
COPY src/ ./src/

RUN apt-get update && apt-get install -y wget unzip gnupg

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update && apt-get install -y google-chrome-stable

RUN google-chrome-stable --version

# RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
#     && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list \
#    && apt-get update \
#    && apt-get install -y google-chrome-stable=115.0.5790.170

RUN wget -q -O /tmp/chromedriver.zip https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/115.0.5790.170/linux64/chromedriver-linux64.zip
RUN unzip -j /tmp/chromedriver.zip chromedriver-linux64/chromedriver -d /usr/local/bin/
RUN rm /tmp/chromedriver.zip

RUN chmod +x /usr/local/bin/chromedriver

# Build the Maven project
RUN mvn clean package -DskipTests

EXPOSE 4444

CMD ["mvn", "test"]
