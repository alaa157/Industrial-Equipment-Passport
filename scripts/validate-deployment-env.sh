#!/usr/bin/env sh
set -eu

env_file="${ENV_FILE:-.env}"
case "$env_file" in
  /*) ;;
  *) env_file="$(pwd)/$env_file" ;;
esac
if [ -f "$env_file" ]; then
  set -a
  . "$env_file"
  set +a
fi

required_vars="
POSTGRES_PASSWORD
JWT_SECRET
REDIS_PASSWORD
RABBITMQ_PASSWORD
GRAFANA_ADMIN_PASSWORD
NEXT_PUBLIC_API_URL
CORS_ALLOWED_ORIGINS
FILE_STORAGE_PATH
"

for variable in $required_vars; do
  eval "value=\${$variable-}"
  if [ -z "$value" ]; then
    echo "Missing required deployment variable: $variable" >&2
    exit 1
  fi
done

case "${DEPLOYMENT_ENV:-development}" in
  production|staging)
    case "$JWT_SECRET" in
      change_this_development_secret_to_a_long_random_value_at_least_32_bytes)
        echo "JWT_SECRET uses the example value" >&2
        exit 1
        ;;
    esac
    if [ "${#JWT_SECRET}" -lt 32 ]; then
      echo "JWT_SECRET must be at least 32 characters" >&2
      exit 1
    fi
    for value in "$POSTGRES_PASSWORD" "$REDIS_PASSWORD" "$RABBITMQ_PASSWORD" "$GRAFANA_ADMIN_PASSWORD"; do
      case "$value" in
        *_dev_password|admin|change_me|change-this*)
          echo "A development/default credential is active" >&2
          exit 1
          ;;
      esac
    done
    ;;
esac

case "$NEXT_PUBLIC_API_URL" in
  http://*|https://*) ;;
  *) echo "NEXT_PUBLIC_API_URL must be an http(s) URL" >&2; exit 1 ;;
esac

case "$FILE_STORAGE_PATH" in
  /*) ;;
  *) echo "FILE_STORAGE_PATH must be an absolute path" >&2; exit 1 ;;
esac

echo "Deployment environment validation passed for ${DEPLOYMENT_ENV:-development}"
