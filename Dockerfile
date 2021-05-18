FROM java:8
COPY build/libs/lock-awesome-1.0.0.jar /lock-awesome.jar
RUN bash -c 'touch /lock-awesome.jar'
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/lock-awesome.jar"]
MAINTAINER guangzheng.li
