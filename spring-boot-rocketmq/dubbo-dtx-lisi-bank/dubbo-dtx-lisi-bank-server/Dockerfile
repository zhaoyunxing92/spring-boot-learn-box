FROM java:8
MAINTAINER zhaoyunxing "2385585770@qq.com"

LABEL version=1.0 \
      desc="分布式事物解决方案实践,rocket、seataim" \
      author="zhaoyunxing"

WORKDIR /dtx
ADD /spring-boot-rocketmq/dubbo-dtx-lisi-bank/dubbo-dtx-lisi-bank-server/target/lisi-server.jar ./lisi-server.jar

# 解决时区问题
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
     && echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT ["java","-jar","./lisi-server.jar"]