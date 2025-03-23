package com.example.app;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.OutputStream;
import java.net.Socket;

public class VoiceSender {

    private Thread sendThread;
    private boolean isSending = false;

    public void startSending(String host, int port) {
        if (isSending) return; // prevent multiple starts
        isSending = true;

        sendThread = new Thread(() -> {
            try {
                Socket socket = new Socket(host, port);
                OutputStream os = socket.getOutputStream();

                int bufferSize = AudioRecord.getMinBufferSize(MainActivity.SAMPLE_RATE,
                        MainActivity.CHANNEL_IN, MainActivity.AUDIO_FORMAT);
                AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        MainActivity.SAMPLE_RATE, MainActivity.CHANNEL_IN,
                        MainActivity.AUDIO_FORMAT, bufferSize);

                byte[] buffer = new byte[bufferSize];
                recorder.startRecording();

                while (isSending && !Thread.currentThread().isInterrupted()) {
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
        });

        sendThread.start();
    }

    public void stopSending() {
        isSending = false;
        if (sendThread != null) {
            sendThread.interrupt();
        }
    }
}