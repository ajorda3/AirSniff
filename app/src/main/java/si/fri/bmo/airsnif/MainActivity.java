package si.fri.bmo.airsnif;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void sniffBT(View view) {
        Button button = (Button) view;
        button.setText("Bluetooth sniff started");

        Intent intent = new Intent(this, BTSniffActivity.class);
        startActivity(intent);

    }

    public void sniffWifi(View view) {

        Button button = (Button) view;
        button.setText("WIFI sniff started");

        Intent intent = new Intent(this, WifiSniffActivity.class);
        startActivity(intent);

    }

    public void sniffGSMSignal(View view) {

        Button button = (Button) view;
        button.setText("GSM sniff started");

        Intent intent = new Intent(this, GSMSSniffActivity.class);

        startActivity(intent);
        //http://developer.xamarin.com/recipes/android/networking/gsm_strength/

    }

    public void sniffGPRS(View view) {

        Button button = (Button) view;
        button.setText("GPRS sniff started");

        Intent intent = new Intent(this, GPRSSniffActivity.class);

        startActivity(intent);
    }

    public void sniffGPS(View view) {

        Button button = (Button) view;
        button.setText("GPS sniff started");

        Intent intent = new Intent(this, GPSSnifActivity.class);

        startActivity(intent);
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
}
