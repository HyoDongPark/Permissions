package com.andrstudy.permissions;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Siro on 2017-11-07.
 */

public class MyReceiver extends BroadcastReceiver {

    private ToggleButton button;

    public MyReceiver(ToggleButton button) {
        this.button = button;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        String strState = "";
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                strState = "ON";
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                strState = "TURNING_ON";
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                strState = "TURNING_OFF";
                break;
            case BluetoothAdapter.STATE_OFF:
                strState = "OFF";
                break;
        }

        if(strState == "ON" || strState == "TURNING_ON")
            this.button.setChecked(true);
        else
            this.button.setChecked(false);

        Toast.makeText(context, "bt "+strState, Toast.LENGTH_SHORT).show();
    }


}
