package com.opticalintelligence.ett

import android.os.Bundle
import android.util.Log;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList
import com.google.mediapipe.framework.AndroidPacketCreator
import com.google.mediapipe.framework.Packet
import com.google.mediapipe.framework.PacketGetter
import java.util.*

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

/** Main activity of MediaPipe face mesh app.  */
class MainActivity : MainActivityBase() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packetCreator: AndroidPacketCreator = processor.getPacketCreator()
        val inputSidePackets: MutableMap<String, Packet> = HashMap()
        inputSidePackets[INPUT_NUM_FACES_SIDE_PACKET_NAME] = packetCreator.createInt32(NUM_FACES)
        processor.setInputSidePackets(inputSidePackets)

        // To show verbose logging, run:
        // adb shell setprop log.tag.MainActivity VERBOSE
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            processor.addPacketCallback(
                OUTPUT_LANDMARKS_STREAM_NAME
            ) { packet ->
                Log.v(TAG, "Received multi face landmarks packet.")
                val multiFaceLandmarks =
                    PacketGetter.getProtoVector(packet, NormalizedLandmarkList.parser())
                Log.v(
                    TAG,
                    ("[TS:"
                            + packet.getTimestamp()
                            ) + "] " + getMultiFaceLandmarksDebugString(
                        multiFaceLandmarks
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val INPUT_NUM_FACES_SIDE_PACKET_NAME = "num_faces"
        private const val OUTPUT_LANDMARKS_STREAM_NAME = "multi_face_landmarks"

        // Max number of faces to detect/process.
        private const val NUM_FACES = 1
        private fun getMultiFaceLandmarksDebugString(
            multiFaceLandmarks: kotlin.collections.List<NormalizedLandmarkList>
        ): String {
            if (multiFaceLandmarks.isEmpty()) {
                return "No face landmarks"
            }
            var multiFaceLandmarksStr = """
                Number of faces detected: ${multiFaceLandmarks.size}
                
                """.trimIndent()
            var faceIndex = 0
            for (landmarks in multiFaceLandmarks) {
                multiFaceLandmarksStr += """	#Face landmarks for face[$faceIndex]: ${landmarks.landmarkCount}
"""
                var landmarkIndex = 0
                for (landmark in landmarks.landmarkList) {
                    multiFaceLandmarksStr += """		Landmark [$landmarkIndex]: (${landmark.x}, ${landmark.y}, ${landmark.z})
"""
                    ++landmarkIndex
                }
                ++faceIndex
            }
            return multiFaceLandmarksStr
        }
    }
}