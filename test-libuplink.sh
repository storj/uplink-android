#!/bin/bash
set -ueo pipefail

# TODO start emulator from cmd or use gradle to do this
# NAME=$(emulator -list-avds | head -n 1)
# emulator -avd $NAME

# Might be easier way than -Pandroid.testInstrumentationRunnerArguments
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.api.key=$GATEWAY_0_API_KEY -Pandroid.testInstrumentationRunnerArguments.storj.sim.host=$SATELLITE_0_ADDR
