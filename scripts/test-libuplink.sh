#!/bin/bash
set -ueo pipefail

# Might be easier way than -Pandroid.testInstrumentationRunnerArguments
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.access=$GATEWAY_0_ACCESS
