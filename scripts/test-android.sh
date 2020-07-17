#!/bin/bash
set -ueo pipefail
set +x

if [ -z "$ANDROID_HOME" ]
then
      echo "\$ANDROID_HOME is not set"
      exit 1
fi

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

go install \
	storj.io/storj/cmd/certificates \
	storj.io/storj/cmd/identity \
	storj.io/storj/cmd/satellite \
	storj.io/storj/cmd/storagenode \
	storj.io/storj/cmd/versioncontrol \
	storj.io/storj/cmd/storj-sim

go install storj.io/gateway

$SCRIPTDIR/../uplink-android/scripts/build-android.sh

PORT=5555
SERIAL=emulator-${PORT}

# setup tmpdir for testfiles and cleanup
TMP=$(mktemp -d -t tmp.XXXXXXXXXX)
cleanup(){
      $ANDROID_HOME/platform-tools/adb -s ${SERIAL} emu kill
      rm -rf "$TMP"
}
trap cleanup EXIT

# start Android emulator
AVD_NAME=android_libuplink_test

export PATH=$ANDROID_HOME/emulator/:$PATH

echo "no" | $ANDROID_HOME/tools/bin/avdmanager create avd --name "${AVD_NAME}" -k "system-images;android-24;default;x86_64" --force
echo "AVD ${AVD_NAME} created."

# -no-accel needs to be added for Jenkins build 
$ANDROID_HOME/emulator/emulator -avd ${AVD_NAME} -port ${PORT} -no-window -no-boot-anim -no-audio -gpu swiftshader_indirect -no-accel 2>&1 &

#Ensure Android Emulator has booted successfully before continuing
# TODO add max number of checks and timeout
while [ "`$ANDROID_HOME/platform-tools/adb shell getprop sys.boot_completed | tr -d '\r' `" != "1" ] ;
do
      echo "waiting for emulator"
      sleep 3
done

export STORJ_NETWORK_DIR=$TMP

STORJ_NETWORK_HOST4=${STORJ_NETWORK_HOST4:-127.0.0.1}

# setup the network
storj-sim -x --host $STORJ_NETWORK_HOST4 network setup

# run tests
storj-sim -x --host $STORJ_NETWORK_HOST4 network test bash "$SCRIPTDIR/test-libuplink.sh"
storj-sim -x --host $STORJ_NETWORK_HOST4 network destroy

