package si.fri.bmo.airsnif;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class BTSniffActivity extends AppCompatActivity {

    BroadcastReceiver mReceiver;
    ListView list;
    boolean hasBT, wasDisabled;
    BluetoothAdapter mBluetoothAdapter;
    ArrayList<String> deviceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btsniff);
        list = (ListView) findViewById(R.id.listView);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceArrayList = new ArrayList<String>();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            hasBT = false;
        }
        else {
            hasBT = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasBT) {
            if (!mBluetoothAdapter.isEnabled()) {
                wasDisabled = true;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            // Create a BroadcastReceiver for ACTION_FOUND
            mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.i("BT action: ", action);
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                        BluetoothClass btClass = device.getBluetoothClass();

                        String classTranslation = "";
                        switch (device.getBluetoothClass().getMajorDeviceClass()){
                            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                                classTranslation = "AUDIO VIDEO";
                                break;
                            case BluetoothClass.Device.Major.COMPUTER:
                                classTranslation = "COMPUTER";
                                break;
                            case BluetoothClass.Device.Major.HEALTH:
                                classTranslation = "HEALTH";
                                break;
                            case BluetoothClass.Device.Major.IMAGING:
                                classTranslation = "IMAGING";
                                break;
                            case BluetoothClass.Device.Major.MISC:
                                classTranslation = "MISC";
                                break;
                            case BluetoothClass.Device.Major.NETWORKING:
                                classTranslation = "NETWORKING";
                                break;
                            case BluetoothClass.Device.Major.PERIPHERAL:
                                classTranslation = "PERIPHERAL";
                                break;
                            case BluetoothClass.Device.Major.PHONE:
                                classTranslation = "PHONE";
                                break;
                            case BluetoothClass.Device.Major.TOY:
                                classTranslation = "TOY";
                                break;
                            case BluetoothClass.Device.Major.UNCATEGORIZED:
                                classTranslation = "PHUNCATEGORIZEDONE";
                                break;
                            case BluetoothClass.Device.Major.WEARABLE:
                                classTranslation = "WEARABLE";
                                break;
                        }

                        String bondState = "";
                        switch (device.getBondState()){
                            case BluetoothDevice.BOND_BONDED:
                                bondState = "BONDED";
                                break;
                            case BluetoothDevice.BOND_BONDING:
                                bondState = "BONDING";
                                break;
                            case BluetoothDevice.BOND_NONE:
                                bondState = "NONE";
                                break;
                        }
                        StringBuffer sb = new StringBuffer();
                        sb.append("Name = ").append(device.getName()).append("\n");
                        sb.append("Address = ").append(device.getAddress()).append("\n");
                        sb.append("BT Major Class = ").append(classTranslation).append("\n");
                        sb.append("Bond state = ").append(bondState).append("\n");
                        sb.append("UUID count = ").append(String.valueOf(device.getUuids().length)).append("\n");
                        deviceArrayList.add(sb.toString());

                        String[] deviceArray = new String[deviceArrayList.size()];
                        deviceArray = deviceArrayList.toArray(deviceArray);


                        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, deviceArray));
                    }
                    else if(mBluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && deviceArrayList.size() == 0){
                        String[] msg = {"No Bluetooth device found", "(Try to refresh)"};
                        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, msg));
                    }
                }
            };
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
            refreshBT();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(hasBT){
            unregisterReceiver(mReceiver);
            if(wasDisabled){
                mBluetoothAdapter.disable();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_btsniff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            refreshBT();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshBT(){
        deviceArrayList.clear();
        String[] msg = {"Scanning for devices", "(it may take 12s or more)"};
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, msg));
        mBluetoothAdapter.startDiscovery();
    }
}
