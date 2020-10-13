#!/usr/bin/env bash
set -o xtrace
set -o errexit

java $JAVA_OPTS $APP_OPTS \
  -Dspring.profiles.active=${SPRING_PROFILE_ENV} \
  -jar application.jar

# One can pass extra commands that can be passed for troubleshooting in case the application terminates immediately
# after startup
exec "$@"
