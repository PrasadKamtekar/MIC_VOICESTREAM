package com.example.app;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VoiceReceiver {

    int SAMPLE_RATE = 16000;
    int CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO;
    int CHANNEL_OUT = AudioFormat.CHANNEL_OUT_MONO;
    int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    public void startReceiving(int port) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept(); // wait for sender

                InputStream is = clientSocket.getInputStream();

                int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_OUT, AUDIO_FORMAT);
                AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC,
                        SAMPLE_RATE, CHANNEL_OUT, AUDIO_FORMAT, bufferSize, AudioTrack.MODE_STREAM);

                byte[] buffer = new byte[bufferSize];
                player.play();

                int read;
                while ((read = is.read(buffer)) > 0) {
                    player.write(buffer, 0, read);
                }

                player.stop();
                player.release();
                clientSocket.close();
                serverSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}