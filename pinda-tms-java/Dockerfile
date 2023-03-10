FROM java:8-alpine
LABEL maintainer="研究院研发组 <research-maint@itcast.cn>"
ARG PACKAGE_PATH
ARG EXPOSE_PORT
ADD ${PACKAGE_PATH:-./} ~/app.jar
EXPOSE ${EXPOSE_PORT}
ENTRYPOINT ["java","-Xmx256m","-jar","~/app.jar"]