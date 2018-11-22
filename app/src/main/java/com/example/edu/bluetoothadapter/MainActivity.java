package com.example.edu.bluetoothadapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listViewPairedDeviceList;
    Button buttonShow;
    private BluetoothAdapter bluetoothAdapter = null;
    Set<BluetoothDevice> pairedDeviceList; // set은 중복된 값을 자동으로 없애줌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewPairedDeviceList = findViewById(R.id.listView);
        buttonShow = findViewById(R.id.buttonShow);
        buttonShow.setOnClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// db에서 데이터 가져와서 인스턴스 화
    }

    @Override
    public void onClick(View v) {
        pairedDeviceList = bluetoothAdapter.getBondedDevices();// 리스트에 데이터 뿌려줌
        ArrayList pairedList = new ArrayList();
        for(BluetoothDevice bt:pairedDeviceList){
            pairedList.add(bt.getName() + "\n" + bt.getAddress());
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedList);
        listViewPairedDeviceList.setAdapter(adapter);
        listViewPairedDeviceList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {// id : 기계 고유의 번호
        String info = ((TextView)view).getText().toString();
        String address = info.substring(info.length() - 17);

        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), BluetoothClientActivity.class);
        intent.putExtra("device_address", address);
        startActivity(intent);
    }
}

