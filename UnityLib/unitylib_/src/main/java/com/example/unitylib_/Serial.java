package com.example.unitylib_;

import android.app.Activity;
import android.widget.Toast;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import static android.util.Log.v;

public class Serial extends UnityPlayerActivity{
    private byte[] data = new byte[1];
    private UsbManager manager;
    private UsbDevice usb;
    private UsbDeviceConnection connection;
    private UsbEndpoint epOUT = null;
    private UsbEndpoint epIN = null;
    private int i;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("unity", "Activity Created");
        manager = (UsbManager) getSystemService(USB_SERVICE);

        updateList();
        if (usb == null) {
            Log.d("unity", "return");
            return;
        }
        if (!manager.hasPermission(usb)) {
            manager.requestPermission(usb,
                    PendingIntent.getBroadcast(Serial.this, 0, new Intent("なにか"), 0));
            return;
        }
        connectDevice();
        i = 10;
        Log.d("unity", Integer.toString(i));
    }
    public static Serial instance(){
        Log.d("Unity", "CreateInstance");
        return new Serial();
    }
    public static void ShowToast() {
        final Activity activity = UnityPlayer.currentActivity;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Hello, World!!", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("unity", "Toast");
    }
    public void StartSerial(){
        Log.d("unity","StarSerial");
    }
    public void Send(){
        Log.d("unity", "Send");
        Log.d("unity", Integer.toString(i));
        data[0] = 15;
        connection.bulkTransfer(epOUT, data, data.length, 0);
    }
    public void Close(){
        connection.close();
    }
    private void updateList() {
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        if (deviceList == null || deviceList.isEmpty()) {
            Log.d("unity","no device found");
        } else {
            String string = "";

            for (String name : deviceList.keySet()) {
                string += name;

                if (deviceList.get(name).getVendorId() == 10755) {
                    string += " (Arduino)\n";
                    usb = deviceList.get(name);
                } else {
                    string += "\n";
                }
            }
            Log.d("unity", string);
        }
    }
    private void connectDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection = manager.openDevice(usb);
                if (!connection.claimInterface(usb.getInterface(1), true)) {
                    connection.close();
                    return;
                }

                connection.controlTransfer(0x21, 34, 0, 0, null, 0, 0);
                connection.controlTransfer(0x21, 32, 0, 0, new byte[] {
                        (byte)0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08
                }, 7, 0);


                epIN = null;
                epOUT = null;
                UsbInterface usbIf = usb.getInterface(1);
                for (int i = 0; i < usbIf.getEndpointCount(); i++) {
                    if (usbIf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                        if (usbIf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
                            epIN = usbIf.getEndpoint(i);
                        else
                            epOUT = usbIf.getEndpoint(i);
                    }
                }
                data[0] = 15;
                connection.bulkTransfer(epOUT, data, data.length, 0);
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                }
                data[0] = 0;
                connection.bulkTransfer(epOUT, data, data.length, 0);
                //connection.close();
            }
        }).start();
    }
}