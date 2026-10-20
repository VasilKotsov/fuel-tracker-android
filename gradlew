#!/usr/bin/env bash
set -euo pipefail

GRADLE_VERSION="8.7"
GRADLE_HOME="${HOME}/.gradle/gradle-${GRADLE_VERSION}"
ARCHIVE_PATH="${HOME}/.gradle/gradle-${GRADLE_VERSION}-bin.zip"
DIST_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"

mkdir -p "${HOME}/.gradle"

if [ ! -x "${GRADLE_HOME}/bin/gradle" ]; then
    if [ ! -f "${ARCHIVE_PATH}" ]; then
        echo "Downloading Gradle ${GRADLE_VERSION}..."
        curl -L "${DIST_URL}" -o "${ARCHIVE_PATH}"
    fi

    echo "Unpacking Gradle ${GRADLE_VERSION}..."
    unzip -q -o "${ARCHIVE_PATH}" -d "${HOME}/.gradle"
fi

exec "${GRADLE_HOME}/bin/gradle" "$@"
