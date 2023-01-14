FROM amazoncorretto:17
ADD target/manufacturing-order-docker.jar manufacturing-order-docker.jar

ENTRYPOINT ["java", "-jar","manufacturing-order-docker.jar"]
EXPOSE 8080