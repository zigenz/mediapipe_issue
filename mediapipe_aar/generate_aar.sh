#!/bin/bash

SOURCE_DIR="${PWD}"
SOURCE_BUILD_FILE_NAME="BUILD.copy_to_dest"
TARGET_FOLDER_NAME="mediapipe/examples/android/src/java/com/google/mediapipe/apps/mediapipe_aar"
TARGET_ROOT_PATH="${PWD}/../third_party/mediapipe"
TARGET_PATH="${TARGET_ROOT_PATH}/${TARGET_FOLDER_NAME}"
BINARY_GRAPH_PATH_NAME="mediapipe/graphs/iris_tracking"
BINARY_GRAPH_OBJ_NAME="iris_tracking_gpu"
AAR_NAME="speqs_mediapipe_aar"
TENSORFLOW_FILES=(
    "mediapipe/modules/face_landmark/face_landmark.tflite"
    "mediapipe/modules/iris_landmark/iris_landmark.tflite"
    "mediapipe/modules/face_detection/face_detection_front.tflite"
)

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

# create local bin folder
if !( test -d "${SOURCE_DIR}/bin" ); then
    mkdir "${SOURCE_DIR}/bin"
    echo "Created folder: ${SOURCE_DIR}/bin"
fi

# create local bin folder
if !( test -d "${SOURCE_DIR}/bin/libs" ); then
    mkdir "${SOURCE_DIR}/bin/libs"
    echo "Created folder: ${SOURCE_DIR}/bin/libs"
fi

cp -f "${TARGET_ROOT_PATH}/bazel-bin/${TARGET_FOLDER_NAME}/${AAR_NAME}.aar" "${SOURCE_DIR}/bin/libs/"

# now build the binary graph
bazel build -c opt "//${BINARY_GRAPH_PATH_NAME}:${BINARY_GRAPH_OBJ_NAME}_binary_graph"
if [ $? -ne 0 ]; then
    echo "Unable to build binary graph!"
    clean
    exit 1;
fi

# create local assets folder
if !( test -d "${SOURCE_DIR}/bin/assets" ); then
    mkdir "${SOURCE_DIR}/bin/assets"
    echo "Created folder: ${SOURCE_DIR}/bin/assets"
fi

# copy to local bin/assets folder
cp -f "${TARGET_ROOT_PATH}/bazel-bin/${BINARY_GRAPH_PATH_NAME}/${BINARY_GRAPH_OBJ_NAME}.binarypb" "${SOURCE_DIR}/bin/assets/"

# copy reamining assets
for f in ${TENSORFLOW_FILES[@]}; do
  cp -f "${TARGET_ROOT_PATH}/${f}" "${SOURCE_DIR}/bin/assets/"
done

exit 0
