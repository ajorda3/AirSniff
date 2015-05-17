package si.fri.bmo.airsnif;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class WifiSniffActivity extends AppCompatActivity {

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_sniff);

        list = (ListView) findViewById(R.id.listView1);
        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_sniff, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++) {
                Log.i("SSID", wifiScanList.get(i).SSID);
                Log.i("BSSID", wifiScanList.get(i).BSSID);
                Log.i("CAPABILITIES", wifiScanList.get(i).capabilities);
                Log.i("LEVEL", String.valueOf(wifiScanList.get(i).level));
                Log.i("FREQUENCY", String.valueOf(wifiScanList.get(i).frequency));

                StringBuffer sb = new StringBuffer();
                sb.append("SSID: ").append(wifiScanList.get(i).SSID).append("\n");
                sb.append("BSSID: ").append(wifiScanList.get(i).BSSID).append("\n");
                sb.append("CAPABILITIES: ").append(wifiScanList.get(i).capabilities).append("\n");
                sb.append("LEVEL: ").append(String.valueOf(wifiScanList.get(i).level)).append("\n");
                sb.append("FREQUENCY: ").append(String.valueOf(wifiScanList.get(i).frequency)).append("\n");

                wifis[i] = (sb.toString());
            }

            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, wifis));
        }
    }
}

