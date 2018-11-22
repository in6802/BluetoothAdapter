package com.example.edu.bluetoothadapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class BluetoothClientActivity extends AppCompatActivity implements View.OnClickListener{

    Button button1, buttonLeft, buttonCenter, buttonRight;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");// UUID 소프트웨어에 고유한 번호
    String address;
    BluetoothSocket bluetoothSocket;// 소켓은 터널이다.
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);

        progressBar = findViewById(R.id.progressBar);
        address = getIntent().getStringExtra("device_address");
        new ConnectBluetoothTask().execute();// 생성하면서 메소드 호출

        findViewById(R.id.buttonUp).setOnClickListener(this);
        findViewById(R.id.buttonLeft).setOnClickListener(this);
        findViewById(R.id.buttonCenter).setOnClickListener(this);
        findViewById(R.id.buttonRight).setOnClickListener(this);
        findViewById(R.id.buttonDown).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(!bluetoothSocket.isConnected())
            new ConnectBluetoothTask().execute();

        String message = null;
        switch(view.getId())
        {
            case R.id.buttonUp:
                message = "U";
                break;

            case R.id.buttonLeft:
                message = "L";
                break;

            case R.id.buttonCenter:
                message = "C";
                break;

            case R.id.buttonRight:
                message = "R";
                break;

            case R.id.buttonDown:
                message = "D";
                break;
        }
        try {
            bluetoothSocket.getOutputStream().write(message.getBytes());//소켓 통신
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ConnectBluetoothTask extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSccess = true;
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... devices) {
            if(bluetoothSocket == null){
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// DB에서 장치 리스트 가져옴
                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
                try {
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);// 통신 연결. 연결 불가능하면 null값이 나옴
                    bluetoothAdapter.cancelDiscovery();
                    bluetoothSocket.connect();//pairing이 끝나면 connect

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}




