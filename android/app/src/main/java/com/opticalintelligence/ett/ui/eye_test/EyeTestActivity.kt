package com.opticalintelligence.ett.ui.eye_test

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList
import com.google.mediapipe.framework.Packet
import com.google.mediapipe.framework.PacketGetter
import com.google.protobuf.InvalidProtocolBufferException
import com.opticalintelligence.ett.R
import java.util.*


/** Main activity of MediaPipe iris tracking app.  */
class EyeTestActivity : EyeTestActivityBase() {
    private var haveAddedSidePackets = false
    override fun onCameraStarted(surfaceTexture: SurfaceTexture?) {
        super.onCameraStarted(surfaceTexture)

        // onCameraStarted gets called each time the activity resumes, but we only want to do this once.
        if (!haveAddedSidePackets) {
            val focalLength: Float = cameraHelper!!.focalLengthPixels
            if (focalLength != Float.MIN_VALUE) {
                val focalLengthSidePacket: Packet =
                    processor!!.packetCreator.createFloat32(focalLength)
                val inputSidePackets: MutableMap<String, Packet> = HashMap()
                inputSidePackets[FOCAL_LENGTH_STREAM_NAME] =
                    focalLengthSidePacket
                processor!!.setInputSidePackets(inputSidePackets)
            }
            haveAddedSidePackets = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val context = findViewById<FrameLayout>(R.id.context)
        if ( null == savedInstanceState ) {
            val newFragment = EyeTestFragment()
            val ft = supportFragmentManager.beginTransaction()
            ft.add( R.id.context, newFragment ).commit()
        }

        // To show verbose logging, run:
        // adb shell setprop log.tag.MainActivity VERBOSE
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            processor!!.addPacketCallback(
                OUTPUT_LANDMARKS_STREAM_NAME
            ) { packet ->
                val landmarksRaw = PacketGetter.getProtoBytes(packet)
                try {
                    val landmarks =
                        NormalizedLandmarkList.parseFrom(landmarksRaw)
                    if (landmarks == null) {
                        Log.v(
                            TAG,
                            "[TS:" + packet.getTimestamp().toString() + "] No landmarks."
                        )
                        return@addPacketCallback
                    }
                    Log.v(
                        TAG,
                        ("[TS:"
                                + packet.getTimestamp()
                                ) + "] #Landmarks for face (including iris): "
                                + landmarks.landmarkCount
                    )
                    Log.v(
                        TAG,
                        getLandmarksDebugString(landmarks)
                    )
                } catch (e: InvalidProtocolBufferException) {
                    Log.e(
                        TAG,
                        "Couldn't Exception received - $e"
                    )
                    return@addPacketCallback
                }
            }
        }
    }

    override val contentViewLayoutResId: Int
         get() = R.layout.activity_eye_test

    companion object {
        private const val TAG = "EyeTestActivity"
        private const val FOCAL_LENGTH_STREAM_NAME = "focal_length_pixel"
        private const val OUTPUT_LANDMARKS_STREAM_NAME = "face_landmarks_with_iris"
        private fun getLandmarksDebugString(landmarks: NormalizedLandmarkList): String {
            var landmarksString = ""
            for ((landmarkIndex, landmark) in landmarks.landmarkList.withIndex()) {
                landmarksString += """		Landmark[$landmarkIndex]: (${landmark.x}, ${landmark.y}, ${landmark.z})
"""
            }
            return landmarksString
        }
    }
}