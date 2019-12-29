FROM oracle/graalvm-ce:19.2.1 as graalvm
COPY . /home/app/micronaut
WORKDIR /home/app/micronaut
RUN gu install native-image
RUN native-image --no-server -cp build/libs/micronaut-*-all.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/micronaut .
ENTRYPOINT ["./micronaut"]
