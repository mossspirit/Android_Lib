package com.example.seriallib;

import android.app.Activity;
import android.widget.Toast;
import com.unity3d.player.UnityPlayer;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.HashMap;
import static android.util.Log.v;

public class Serial extends AppCompatActivity{
    private byte[] data;
    private UsbManager manager;
    private UsbDevice usb;
    private UsbDeviceConnection connection;
    private  UsbEndpoint epOUT = null;

    public static void ShowToast() {
        final Activity activity = UnityPlayer.currentActivity;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Hello, World!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void StartSerial(){
        final  Activity activity = UnityPlayer.currentActivity;
        manager = (UsbManager)getSystemService(USB_SERVICE);

    }
}
