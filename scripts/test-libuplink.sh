#!/bin/bash
set -ueo pipefail

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# update access to android emulator localhost alias
ACCESS=$(go run "$SCRIPTDIR"/update-access-sataddr.go "10.0.2.2:10000" "$GATEWAY_0_ACCESS")
./gradlew clean connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.access=$ACCESS
