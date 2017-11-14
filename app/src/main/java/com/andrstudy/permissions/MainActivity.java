package com.andrstudy.permissions;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnClickListener{
    private MyReceiver receiver;
    private TextView textView;
    private LocationManager locationManager;
    private ToggleButton toggleButton;
    private BluetoothAdapter mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    boolean hasBluetooth = (mbluetoothAdapter == null);
    //---------------------------------------------------------------------------
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String strLocation = "LAT:" + location.getLatitude();
            strLocation += "LNG:" + location.getLongitude();
            textView.setText(strLocation);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    //-----------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.naver.com");

        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(this);

        String val = BluetoothAdapter.ACTION_STATE_CHANGED;


        textView = (TextView)findViewById(R.id.textView);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        receiver = new MyReceiver(toggleButton);
        this.registerReceiver(receiver , filter);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initLocationManager();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION} , 1001);
        }
    }

    private void initLocationManager() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
        } catch (SecurityException e) {
            Toast.makeText(this , "권한이 필요합니다." , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1001) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                initLocationManager();
            else
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view) {
        if(toggleButton.isChecked()) {
            if(hasBluetooth && !mbluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        else {
            if(hasBluetooth && mbluetoothAdapter.isEnabled()) {
                boolean isDisabling = mbluetoothAdapter.disable();
                if(!isDisabling) {

                }
            }
        }
    }
}
