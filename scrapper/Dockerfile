FROM openjdk:21
LABEL authors="lostevil"

COPY ./scrapper/target/scrapper.jar scrapper.jar

ENTRYPOINT ["java","-jar","/scrapper.jar"]
