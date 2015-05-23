package si.fri.bmo.airsnif;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Iterator;


public class GPSSnifActivity extends AppCompatActivity {

    ListView list;
    LocationManager locManager;
    GPSLISTENER gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpssnif);
        list = (ListView) findViewById(R.id.listView);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        String[] msg = {"Scanning for satellites"};
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, msg));
    }

    @Override
    protected void onResume() {
        super.onResume();

        gpsListener = new GPSLISTENER();
        locManager.addGpsStatusListener(gpsListener);
        locManager.requestLocationUpdates(locManager.GPS_PROVIDER, 60000, 25, gpsListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        locManager.removeGpsStatusListener(gpsListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpssnif, menu);
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

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public class GPSLISTENER implements GpsStatus.Listener, LocationListener {

        @Override
        public void onGpsStatusChanged(int event) {
            String[] satelliteData = new String[1];
            GpsStatus statusGPS = locManager.getGpsStatus(null);
            int maxSatellites = statusGPS.getMaxSatellites();
            Log.i("Satellite count", String.valueOf(maxSatellites));
            boolean failure = false;
            if (statusGPS != null && maxSatellites > 0) {
                satelliteData = new String[maxSatellites];
                Iterator<GpsSatellite> iterGPS = statusGPS.getSatellites().iterator();
                if(!iterGPS.hasNext())
                    failure = true;
                int i = 1;
                Log.i("Satellite", String.valueOf(iterGPS.hasNext()));
                while (iterGPS.hasNext()) {
                    GpsSatellite gps = iterGPS.next();
                    // Degrees from North
                    float azimuth = gps.getAzimuth();
                    // elevation in degrees
                    float elevation = gps.getElevation();
                    // pseudo-random number for the satellite
                    int randomNum = gps.getPrn();
                    // signal-to-noise ratio. Signal quality
                    float signalToNoise = gps.getSnr();
                    // do we have Almanac data = long term course orbital corrections
                    boolean hasAlmanac = gps.hasAlmanac();
                    // do we have Ephemeris data = short term course orbital corrections
                    boolean hasEphemeris = gps.hasEphemeris();
                    // was used in last GPS reading
                    boolean wasUsed = gps.usedInFix();

                    Log.i("Satellite", String.valueOf(i));
                    Log.i("SIGNAL QUALITY", String.valueOf(signalToNoise));
                    Log.i("RANDOM NUMBER", String.valueOf(randomNum));
                    Log.i("AZIMUTH", String.valueOf(azimuth));
                    Log.i("ELEVATION", String.valueOf(elevation));
                    Log.i("ALMANAC", String.valueOf(hasAlmanac));
                    Log.i("EPHEMERIS", String.valueOf(hasEphemeris));
                    Log.i("USED", String.valueOf(wasUsed));


                    StringBuffer sb = new StringBuffer();
                    sb.append("Satellite ").append(i).append("\n");
                    sb.append("SIGNAL QUALITY = ").append(signalToNoise).append("\n");
                    sb.append("RANDOM NUMBER = ").append(randomNum).append("\n");
                    sb.append("AZIMUTH = ").append(azimuth).append(" °").append("\n");
                    sb.append("ELEVATION = ").append(elevation).append(" °").append("\n");
                    sb.append("HAS ALMANAC DATA = ").append(hasAlmanac).append("\n");
                    sb.append("HAS EPHEMERIS DATA = ").append(hasEphemeris).append("\n");
                    sb.append("WAS USED BY US = ").append(wasUsed).append("\n");
                    satelliteData[i - 1] = sb.toString();
                    i++;
                }
            } else {
                failure = true;
            }
            if(failure){
                satelliteData = new String[1];
                satelliteData[0] = "No satellite detected";
            }

            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, satelliteData));
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}

