# Introduction 
To be able to link MediaPipe to our Android application, an AAR file must first be generated.

To generate the AAR file, run generate_aar.sh in this directory.

Full instructions can be found here: https://github.com/google/mediapipe/blob/master/docs/getting_started/android_archive_library.md

A sample manifest is available at:
mediapipe/examples/android/src/java/com/google/mediapipe/apps/basic.  The various \<metadata> variables to be set as follows:
* "applicationId": "com.google.mediapipe.apps.facemeshgpu"
* "appName": "Face Mesh",
* "mainActivity": ".MainActivity",
* "cameraFacingFront": "True",
* "binaryGraphName": "face_mesh_mobile_gpu.binarypb",
* "inputVideoStreamName": "input_video",
* "outputVideoStreamName": "output_video",
* "flipFramesVertically": "True",
* "converterNumBuffers": "2",
