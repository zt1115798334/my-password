FROM openjdk:8u282-oraclelinux8
MAINTAINER zt
VOLUME /tmp

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-jar","app.jar"]
#ENTRYPOINT ["java","-jar","-XX:MaxMetaspaceSize=1g","-Xms32g","-Xmx32g","-Xmn16g","-Xss1024k","-XX:ParallelGCThreads=8","-XX:+UseAdaptiveSizePolicy","-Xloggc:/logs/app-gc.log","app.jar"]
