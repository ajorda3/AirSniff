package si.fri.bmo.airsnif;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.telephony.SignalStrength;

public class GSMSSniffActivity extends AppCompatActivity {

    final String TAG = "PhoneSignalStrengthActivity";

    TextView textViewLog;
    EditText editTextLog;
    SignalStrengthListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsmssniff);

        editTextLog = (EditText) findViewById(R.id.editTextLog);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mListener = new SignalStrengthListener();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		tm.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
//		tm.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTH|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        tm.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(mListener, PhoneStateListener.LISTEN_NONE);	// LISTEN_NONE : Stop listening for updates.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gsmssniff, menu);
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

    public class SignalStrengthListener extends PhoneStateListener {

//		@Override
//		public void onSignalStrengthChanged(int asu) {
//			String str = asu + &quot;/&quot;+String.valueOf(-113+2*asu)+&quot;dBm&quot;;
//			editTextLog.append(str+&quot;\n&quot;);
//			Log.v(TAG,&quot;asu=&quot;+str);
//		}

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            boolean	isGsm = signalStrength.isGsm();
            // Get the CDMA RSSI value in dBm
            int	iCdmaDbm = signalStrength.getCdmaDbm();
            // Get the CDMA Ec/Io value in dB*10
            int	iCdmaEcio = signalStrength.getCdmaEcio();
            // Get the EVDO RSSI value in dBm
            int	iEvdoDbm = signalStrength.getEvdoDbm();
            // Get the EVDO Ec/Io value in dB*10
            int	iEvdoEcio = signalStrength.getEvdoEcio();
            // Get the signal to noise ratio. Valid values are 0-8. 8 is the highest.
            int	iEvdoSnr = signalStrength.getEvdoSnr();
            // Get the GSM bit error rate (0-7, 99) as defined in TS 27.007 8.5
            int	iGsmBitErrorRate = signalStrength.getGsmBitErrorRate();
            // Get the GSM Signal Strength, valid values are (0-31, 99) as defined in TS 27.007 8.5
            int	iGsmSignalStrength = signalStrength.getGsmSignalStrength();

            StringBuffer sb = new StringBuffer();
            sb.append("GSM = ").append(isGsm).append("\n");
            sb.append("GSM Signal Strength= ").append(iGsmSignalStrength).append("\n");
            sb.append("GSM Bit Error Rate= ").append(iGsmBitErrorRate).append("\n");
            sb.append("CDMA RSSI= ").append(iCdmaDbm).append(" dBm").append("\n");
            sb.append("CDMA Ec/Io= ").append(iCdmaEcio).append("dB*10").append("\n");
            sb.append("EVDO RSSI= ").append(iEvdoDbm).append(" dBm").append("\n");
            sb.append("EVDO Ec/Io= ").append(iEvdoEcio).append("dB*10").append("\n");
            sb.append("EVDO SNR= ").append(iEvdoSnr).append("\n");


            //Log.v(TAG, sb.toString());
            editTextLog.append(sb.toString());
        }
    }
}
