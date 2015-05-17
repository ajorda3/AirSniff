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

    public void onButtonClick(View view) {
        Button button = (Button) view;
        button.setText("Clicked2");

        Intent intent = new Intent(this, NewActivity.class);
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
        button.setText("Signal sniff started");

        Intent intent = new Intent(this, GSMSSniffActivity.class);

        startActivity(intent);
        //http://developer.xamarin.com/recipes/android/networking/gsm_strength/

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
