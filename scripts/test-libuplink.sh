#!/bin/bash
set -ueo pipefail

# Might be easier way than -Pandroid.testInstrumentationRunnerArguments
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.scope=$GATEWAY_0_ACCESS
