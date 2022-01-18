# build gcsfuse
FROM golang:1.17-alpine as gcsfuse
RUN apk add --no-cache git musl-dev \
    && go get -v -u github.com/googlecloudplatform/gcsfuse

# build exhibitor
FROM maven:3.8-eclipse-temurin-11-alpine as builder

WORKDIR /exhibitor
COPY . .

RUN mvn -B -DskipTests=true install \
    && mvn -B -nsu -f exhibitor-standalone/src/main/resources/buildscripts/standalone/maven/pom.xml package

FROM eclipse-temurin:11-jdk-alpine
LABEL maintainer "Bringg DevOps <devops@bringg.com>"

ARG ZK_VERSION="3.6.3"
ENV ZK_RELEASE="http://archive.apache.org/dist/zookeeper/zookeeper-$ZK_VERSION/apache-zookeeper-$ZK_VERSION-bin.tar.gz"

RUN \
    # Install required packages
    apk add --no-cache bash fuse procps tini \
    \
    # Alpine doesn't have /opt dir
    && mkdir -p /opt \
    \
    # Install ZK
    && wget -qO- $ZK_RELEASE | tar -xvz -C /opt \
    && ln -s /opt/apache-zookeeper-$ZK_VERSION-bin /opt/zookeeper

# Add the optional web.xml for authentication and the wrapper script to setup configs
COPY docker-entrypoint.sh /usr/local/bin/

# Copy files from build containers
COPY --from=gcsfuse /go/bin/gcsfuse /usr/local/bin/
COPY --from=builder /exhibitor/exhibitor-standalone/src/main/resources/buildscripts/standalone/maven/target/exhibitor.jar /opt/exhibitor/exhibitor.jar

WORKDIR /opt/exhibitor
EXPOSE 2181 2888 3888 8181
ENTRYPOINT ["/sbin/tini", "-g", "--", "docker-entrypoint.sh"]
