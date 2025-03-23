package com.example.app;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.OutputStream;
import java.net.Socket;

public class VoiceSender {

    int SAMPLE_RATE = 16000;
    int CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO;
    int CHANNEL_OUT = AudioFormat.CHANNEL_OUT_MONO;
    int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;


    public void startSending(String host, int port) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(host, port);
                OutputStream os = socket.getOutputStream();

                int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_IN, AUDIO_FORMAT);
                AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE, CHANNEL_IN, AUDIO_FORMAT, bufferSize);

                byte[] buffer = new byte[bufferSize];
                recorder.startRecording();

                while (!Thread.currentThread().isInterrupted()) {
                    int read = recorder.read(buffer, 0, buffer.length);
                    if (read > 0) {
                        os.write(buffer, 0, read);
                    }
                }

                recorder.stop();
                recorder.release();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}