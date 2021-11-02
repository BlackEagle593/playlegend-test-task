# PaperSpigot
FROM openjdk:17-alpine as paper

ARG MC_VERSION=1.17.1

WORKDIR /root

RUN apk update && \
    apk add ca-certificates && \
    update-ca-certificates && \
    apk add jq

COPY --chmod=0700 papermc.sh .
RUN ./papermc.sh


# Plugin Builder
FROM gradle:jdk17 as builder

# Copy source files
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

# Build with gradle
RUN gradle shadowJar


# Server
FROM openjdk:17-alpine

RUN addgroup --system minecraft && adduser --system --home /minecraft --ingroup minecraft minecraft

USER minecraft
WORKDIR /minecraft

ENV MEMORY_JVM=512M
ENV JVM_ARGUMENTS="-XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:-UseParallelGC -XX:-UseG1GC -XX:+UseZGC -XX:MaxGCPauseMillis=100 -XX:TargetSurvivorRatio=90 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled"
ENV MC_ARGUMENTS="-Dcom.mojang.eula.agree=true"
ENV MC_PARAMETERS="-nogui"

# Copy paper.jar
COPY --chown=minecraft --from=paper /root/cache/patched_*.jar paper.jar

# Copy plugin
COPY --chown=minecraft --from=builder /home/gradle/src/build/libs/*-all.jar plugins/

EXPOSE 25565

HEALTHCHECK --start-period=15s --interval=15s --timeout=5s CMD echo -e '\x0f\x00\x00\x09\x31\x32\x37\x2e\x30\x2e\x30\x2e\x31\x63\xdd\x01\x01\x00' | nc 127.0.0.1 25565 -w 1 | grep version

ENTRYPOINT java -Xms${MEMORY_JVM} ${JVM_ARGUMENTS} ${MC_ARGUMENTS} -jar paper.jar ${MC_PARAMETERS}
