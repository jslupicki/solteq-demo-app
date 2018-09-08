FROM anapsix/alpine-java:latest
ARG JAR_FILE
COPY target/${JAR_FILE} demo-app.jar
CMD java -Xdebug -Xrunjdwp:transport=dt_socket,address=8081,server=y,suspend=n -jar demo-app.jar
