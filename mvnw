#!/bin/sh

set -eu

BASE_DIR=$(
  CDPATH= cd -- "$(dirname "$0")" && pwd
)
WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"
PROPERTIES_FILE="$WRAPPER_DIR/maven-wrapper.properties"

if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "Missing $PROPERTIES_FILE" >&2
  exit 1
fi

DISTRIBUTION_URL=$(
  sed -n 's/^distributionUrl=//p' "$PROPERTIES_FILE" | head -n 1
)

if [ -z "$DISTRIBUTION_URL" ]; then
  echo "distributionUrl is not configured in $PROPERTIES_FILE" >&2
  exit 1
fi

ARCHIVE_NAME=${DISTRIBUTION_URL##*/}
MAVEN_DIR_NAME=${ARCHIVE_NAME%-bin.zip}
MAVEN_HOME="$WRAPPER_DIR/$MAVEN_DIR_NAME"
MAVEN_BIN="$MAVEN_HOME/bin/mvn"

if [ ! -x "$MAVEN_BIN" ]; then
  if ! command -v curl >/dev/null 2>&1; then
    echo "curl is required to download Maven automatically." >&2
    exit 1
  fi
  if ! command -v unzip >/dev/null 2>&1; then
    echo "unzip is required to extract Maven automatically." >&2
    exit 1
  fi

  mkdir -p "$WRAPPER_DIR"
  TMP_DIR=$(mktemp -d)
  trap 'rm -rf "$TMP_DIR"' EXIT INT TERM
  ARCHIVE_PATH="$TMP_DIR/$ARCHIVE_NAME"

  echo "Downloading Maven from $DISTRIBUTION_URL"
  curl -fsSL "$DISTRIBUTION_URL" -o "$ARCHIVE_PATH"
  unzip -q "$ARCHIVE_PATH" -d "$WRAPPER_DIR"
fi

exec "$MAVEN_BIN" "$@"
