FROM oracle/graalvm-ce:19.3.0-java11 as graalvm
COPY . /home/app/medium-micronaut-r2dbc-example
WORKDIR /home/app/medium-micronaut-r2dbc-example
RUN gu install native-image
RUN native-image --no-server -cp build/libs/medium-micronaut-r2dbc-example-*-all.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/medium-micronaut-r2dbc-example .
ENTRYPOINT ["./medium-micronaut-r2dbc-example"]
