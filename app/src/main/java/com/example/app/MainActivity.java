package com.example.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    EditText ipInput;
    Button btnSend, btnReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipInput = findViewById(R.id.ip_input);
        btnSend = findViewById(R.id.btn_send);
        btnReceive = findViewById(R.id.btn_receive);

        // Request mic + network permissions
        if (!checkPermissions()) {
            requestPermissions();
        }

        btnReceive.setOnClickListener(v -> {
            new VoiceReceiver().startReceiving(50005);
            Toast.makeText(this, "Started Receiving", Toast.LENGTH_SHORT).show();
        });

        btnSend.setOnClickListener(v -> {
            String ip = ipInput.getText().toString();
            if (ip.isEmpty()) {
                Toast.makeText(this, "Enter Receiver IP Address", Toast.LENGTH_SHORT).show();
                return;
            }
            new VoiceSender().startSending(ip, 50005);
            Toast.makeText(this, "Started Sending to " + ip, Toast.LENGTH_SHORT).show();
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission required!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}