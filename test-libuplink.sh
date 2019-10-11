#!/bin/bash
set -ueo pipefail

# Might be easier way than -Pandroid.testInstrumentationRunnerArguments
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.api.key=$GATEWAY_0_API_KEY -Pandroid.testInstrumentationRunnerArguments.storj.sim.host=$SATELLITE_0_ADDR
