package com.example.app;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    public static final int SAMPLE_RATE = 16000;
    public static final int CHANNEL_IN = android.media.AudioFormat.CHANNEL_IN_MONO;
    public static final int CHANNEL_OUT = android.media.AudioFormat.CHANNEL_OUT_MONO;
    public static final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;

    private static final int PERMISSION_REQUEST_CODE = 1;

    EditText ipInput;
    Button btnSend, btnReceive;

    boolean isSending = false;
    boolean isReceiving = false;

    VoiceSender voiceSender = new VoiceSender();
    VoiceReceiver voiceReceiver = new VoiceReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipInput = findViewById(R.id.ip_input);
        btnSend = findViewById(R.id.btn_send);
        btnReceive = findViewById(R.id.btn_receive);

        // Check & request permission
        if (!checkPermissions()) {
            requestPermissions();
        }

        // Toggle sending
        btnSend.setOnClickListener(v -> {
            if (!isSending) {
                String ip = ipInput.getText().toString().trim();
                if (ip.isEmpty()) {
                    Toast.makeText(this, "Please enter Receiver IP Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                voiceSender.startSending(ip, 50005);
                isSending = true;
                btnSend.setText("Stop Sending");
                Toast.makeText(this, "Voice sending started", Toast.LENGTH_SHORT).show();
            } else {
                voiceSender.stopSending();
                isSending = false;
                btnSend.setText("Start Sending");
                Toast.makeText(this, "Voice sending stopped", Toast.LENGTH_SHORT).show();
            }
        });

        // Toggle receiving
        btnReceive.setOnClickListener(v -> {
            if (!isReceiving) {
                voiceReceiver.startReceiving(50005);
                isReceiving = true;
                btnReceive.setText("Stop Receiving");
                Toast.makeText(this, "Voice receiving started", Toast.LENGTH_SHORT).show();
            } else {
                voiceReceiver.stopReceiving();
                isReceiving = false;
                btnReceive.setText("Start Receiving");
                Toast.makeText(this, "Voice receiving stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}