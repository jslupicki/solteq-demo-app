FROM anapsix/alpine-java:latest
ARG JAR_FILE
COPY target/${JAR_FILE} demo-app.jar
CMD java -jar demo-app.jar
