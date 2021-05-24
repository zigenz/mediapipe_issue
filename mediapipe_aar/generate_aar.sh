#!/bin/bash

SOURCE_DIR="${PWD}"
SOURCE_BUILD_FILE_NAME="BUILD.copy_to_dest"
TARGET_FOLDER_NAME="mediapipe/examples/android/src/java/com/google/mediapipe/apps/mediapipe_aar"
TARGET_ROOT_PATH="${PWD}/../third_party/mediapipe"
TARGET_PATH="${TARGET_ROOT_PATH}/${TARGET_FOLDER_NAME}"
AAR_NAME="speqs_mediapipe_aar"

function clean()
{
    if test -f "${TARGET_PATH}"; then
        rm -rf "${TARGET_PATH}"
    fi
}

function mkdir_target()
{
    if !( test -f "${TARGET_PATH}" ); then
        mkdir "${TARGET_PATH}"
        echo "Created folder: ${TARGET_PATH}"
    fi
}

clean

# make the directory
mkdir_target
if [ $? -ne 0 ]; then
    echo "Cannot create target directory ()${TARGET_PATH}!"
    clean
    exit 1;
fi


# first copy the build file to the destination
cp "${SOURCE_BUILD_FILE_NAME}" "${TARGET_PATH}/BUILD"
if [ $? -ne 0 ]; then
    echo "Copying build file to target failed!"
    clean
    exit 1;
fi

# change directory to the target
cd "${TARGET_PATH}"
if [ $? -ne 0 ]; then
    echo "Cannot switch to target path (${TARGET_PATH})!"
    clean
    exit 1;
fi
echo "Switched path to: ${TARGET_PATH}"

# invoke the bazel command
#echo "Calling \"bazel build -c opt --strip=ALWAYS --host_crosstool_top=@bazel_tools//tools/cpp:toolchain --fat_apk_cpu=arm64-v8a,armeabi-v7a //${TARGET_FOLDER_NAME}:speqs_mediapipe_aar.aar\""
bazel build -c opt --strip=ALWAYS --host_crosstool_top=@bazel_tools//tools/cpp:toolchain --fat_apk_cpu=arm64-v8a,armeabi-v7a //${TARGET_FOLDER_NAME}:${AAR_NAME}.aar
if [ $? -ne 0 ]; then
    echo "Build failed!"
    clean
    exit 1;
fi

# copy to local bin folder
if !( test -f "${SOURCE_DIR}/bin" ); then
    mkdir "${SOURCE_DIR}/bin"
    echo "Created folder: ${SOURCE_DIR}/bin"
fi

cp "${TARGET_ROOT_PATH}/bazel-bin/${TARGET_FOLDER_NAME}/${AAR_NAME}.aar" "${SOURCE_DIR}/bin/"

exit 0
