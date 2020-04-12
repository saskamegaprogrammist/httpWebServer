FROM openjdk:8
COPY ./out/production/httpWebServer/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","Main"]