FROM openjdk:11-jre-slim


CMD ["./bin/docker-entrypoint.sh"]

RUN mkdir -p config/docker /gameofthree/bin
WORKDIR /gameofthree

COPY ./bin/docker-entrypoint.sh /gameofthree/bin/
COPY ./build/libs/application.jar /gameofthree/

HEALTHCHECK --interval=5m --timeout=3s \
  CMD curl -f http://localhost:${PORT:8080}/internal/health || exit 1