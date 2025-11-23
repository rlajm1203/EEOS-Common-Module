#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

GRADLE_ARGS=()

if [[ -z "${GITHUB_USERNAME:-}" && -z "${GPR_USER:-}" ]]; then
  echo "GitHub Packages 인증을 위해 GITHUB_USERNAME (또는 GPR_USER)을 설정해 주세요." >&2
  exit 1
fi

if [[ -z "${GITHUB_TOKEN:-}" && -z "${GPR_KEY:-}" ]]; then
  echo "GitHub Packages 인증을 위해 GITHUB_TOKEN (또는 GPR_KEY)을 설정해 주세요." >&2
  exit 1
fi

if [[ -n "${GITHUB_USERNAME:-}" ]]; then
  GRADLE_ARGS+=("-Pgpr.user=${GITHUB_USERNAME}")
elif [[ -n "${GPR_USER:-}" ]]; then
  GRADLE_ARGS+=("-Pgpr.user=${GPR_USER}")
fi

if [[ -n "${GITHUB_TOKEN:-}" ]]; then
  GRADLE_ARGS+=("-Pgpr.key=${GITHUB_TOKEN}")
elif [[ -n "${GPR_KEY:-}" ]]; then
  GRADLE_ARGS+=("-Pgpr.key=${GPR_KEY}")
fi

./gradlew clean publish "${GRADLE_ARGS[@]}"
