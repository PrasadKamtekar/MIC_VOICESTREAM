package com.example.app;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VoiceReceiver {

    private Thread receiveThread;
    private boolean isReceiving = false;

    public void startReceiving(int port) {
        if (isReceiving) return;
        isReceiving = true;

        receiveThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                InputStream is = clientSocket.getInputStream();

                int bufferSize = AudioTrack.getMinBufferSize(MainActivity.SAMPLE_RATE,
                        MainActivity.CHANNEL_OUT, MainActivity.AUDIO_FORMAT);
                AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC,
                        MainActivity.SAMPLE_RATE, MainActivity.CHANNEL_OUT,
                        MainActivity.AUDIO_FORMAT, bufferSize, AudioTrack.MODE_STREAM);

                byte[] buffer = new byte[bufferSize];
                player.play();

                int read;
                while (isReceiving && (read = is.read(buffer)) > 0) {
                    player.write(buffer, 0, read);
                }

                player.stop();
                player.release();
                clientSocket.close();
                serverSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        receiveThread.start();
    }

    public void stopReceiving() {
        isReceiving = false;
        if (receiveThread != null) {
            receiveThread.interrupt();
        }
    }
}