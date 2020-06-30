#!/bin/bash
set -ueo pipefail

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# Might be easier way than -Pandroid.testInstrumentationRunnerArguments
# ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.access=$GATEWAY_0_ACCESS
export LD_LIBRARY_PATH=$SCRIPTDIR/../.build/
./gradlew -Ptesting test
