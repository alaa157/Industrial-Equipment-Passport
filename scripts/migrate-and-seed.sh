#!/usr/bin/env sh
set -eu

compose_file="${COMPOSE_FILE:-docker-compose.yml}"
env_file="${ENV_FILE:-.env}"

if [ ! -f "$env_file" ]; then
  echo "Environment file not found: $env_file" >&2
  exit 1
fi

docker compose --env-file "$env_file" -f "$compose_file" run --rm \
  -e SPRING_MAIN_WEB_APPLICATION_TYPE=none \
  migration-auth
docker compose --env-file "$env_file" -f "$compose_file" run --rm \
  -e SPRING_MAIN_WEB_APPLICATION_TYPE=none \
  migration-equipment
docker compose --env-file "$env_file" -f "$compose_file" run --rm \
  -e SPRING_MAIN_WEB_APPLICATION_TYPE=none \
  migration-maintenance
docker compose --env-file "$env_file" -f "$compose_file" run --rm \
  -e SPRING_MAIN_WEB_APPLICATION_TYPE=none \
  migration-notification
docker compose --env-file "$env_file" -f "$compose_file" run --rm \
  -e SPRING_MAIN_WEB_APPLICATION_TYPE=none \
  migration-audit
