FROM alpine:3.11 AS libs

RUN apk add openjdk14 --repository=http://dl-3.alpinelinux.org/alpine/edge/testing/
RUN apk add vim
RUN apk add maven

FROM libs AS app
COPY . /backend
EXPOSE 8082
RUN mkdir /backend/log
WORKDIR /backend

RUN mvn install
WORKDIR /backend/target

CMD ["java", "-jar", "systemyRozproszone-0.0.1-SNAPSHOT.jar", "--spring.config.location=/backend/src/main/resources/application.properties"]

