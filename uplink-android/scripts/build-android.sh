#!/bin/bash

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

WORK=$SCRIPTDIR/../src/main
GOOS=android GOARCH=arm   CC=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi21-clang CXX=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi21-clang++ CGO_ENABLED=1 GOARM=7 go build -v -buildmode=c-shared -o=$WORK/jniLibs/armeabi-v7a/libuplink.so storj.io/uplink-c
GOOS=android GOARCH=arm64 CC=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android21-clang    CXX=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android21-clang++    CGO_ENABLED=1 go build -v -buildmode=c-shared         -o=$WORK/jniLibs/arm64-v8a/libuplink.so storj.io/uplink-c
GOOS=android GOARCH=386   CC=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android21-clang       CXX=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android21-clang++       CGO_ENABLED=1 go build -v -buildmode=c-shared         -o=$WORK/jniLibs/x86/libuplink.so storj.io/uplink-c
GOOS=android GOARCH=amd64 CC=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android21-clang     CXX=$ANDROID_HOME/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/x86_64-linux-android21-clang++     CGO_ENABLED=1 go build -v -buildmode=c-shared         -o=$WORK/jniLibs/x86_64/libuplink.so storj.io/uplink-c

