FROM gradle:8.12.0-jdk21 AS builder
ENV GRADLE_USER_HOME=/cache
ENV WORKDIR=/usr/src/app
WORKDIR $WORKDIR
RUN --mount=type=bind,target=.,rw \
    --mount=type=cache,target=$GRADLE_USER_HOME \
    ./gradlew -i jooqCodegen &&  \
    ./gradlew -i bootJar --stacktrace && \
    mv $WORKDIR/build/libs/dbfirstcookie.jar /dbfirstcookie.jar
RUN echo "Cache stage completed" && ls -la /dbfirstcookie.jar

FROM bellsoft/liberica-openjdk-alpine:21 AS runner
ENV WORKDIR=/usr/src/app
WORKDIR $WORKDIR
EXPOSE 8080
RUN mkdir -p /var/log
RUN apk add -q tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
COPY --from=builder /dbfirstcookie.jar ./dbfirstcookie.jar
CMD ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-jar", "./dbfirstcookie.jar"]
