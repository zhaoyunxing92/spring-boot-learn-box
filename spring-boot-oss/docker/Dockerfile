FROM java:8
LABEL version="1.0" description="oss sts" by="zhaoyunxing"
MAINTAINER zhaoyunxing
WORKDIR /oss
ADD ../target/spring-boot-oss.jar ./spring-boot-oss.jar
#COPY ../target/spring-boot-oss.jar spring-boot-oss.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spring-boot-oss.jar"]