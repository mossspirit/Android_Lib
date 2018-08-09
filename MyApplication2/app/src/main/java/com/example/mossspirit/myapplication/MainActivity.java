package com.example.mossspirit.myapplication;

import java.io.*;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.*;
import com.hoho.android.usbserial.driver.*;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import static android.util.Log.v;

public class MainActivity extends AppCompatActivity {

    private TextView textView0, textView1, textView2, textView3;

    private Boolean btn0 = false;
    private Boolean btn1 = false;
    private Boolean btn2 = false;
    private Boolean btn3 = false;
    private byte[] data = new byte[1];
    private int[] state = new int[4];
    private UsbManager manager;
    private UsbDevice usb;
    private UsbDeviceConnection connection;

    private UsbEndpoint epIN = null;
    private UsbEndpoint epOUT = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data[0] = 0;
        manager = (UsbManager)getSystemService(USB_SERVICE);
        updateList();
        if(usb == null){
            return;
        }
        if(!manager.hasPermission(usb)){
            manager.requestPermission(usb,
                    PendingIntent.getBroadcast(MainActivity.this, 0, new Intent("なにか"), 0));
            return;
        }
        connectDevice();

        //Buttonを追加
        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button send = findViewById(R.id.send);

        //Textを追加
        textView0 = findViewById(R.id.textView0);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flagがtrueの時
                if (btn0) {
                    textView0.setText("OFF");
                    state[0] = 0;
                    btn0 = false;
                }
                // flagがfalseの時
                else {
                    textView0.setText("ON");
                    state[0] = 1;
                    btn0 = true;
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flagがtrueの時
                if (btn1) {
                    textView1.setText("OFF");
                    state[1] = 0;
                    btn1 = false;
                }
                // flagがfalseの時
                else {
                    textView1.setText("ON");
                    state[1] = 1;
                    btn1 = true;
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flagがtrueの時
                if (btn2) {
                    textView2.setText("OFF");
                    state[2] = 0;
                    btn2 = false;
                }
                // flagがfalseの時
                else {
                    //textView.setText(R.string.world);
                    textView2.setText("ON");
                    state[2] = 1;
                    btn2 = true;
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flagがtrueの時
                if (btn3) {
                    textView3.setText("OFF");
                    state[3] = 0;
                    btn3 = false;
                }
                // flagがfalseの時
                else {
                    textView3.setText("ON");
                    state[3] = 1;
                    btn3 = true;
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send();
            }
        });
    }
    private void updateList() {
      HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

      if (deviceList == null || deviceList.isEmpty()) {
          Log.d("arduino","no device found");
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
            Log.d("arduino", string);
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
                data[0] = 0;
                connection.bulkTransfer(epOUT, data, data.length, 0);
                //connection.close();
            }
        }).start();
    }
    private void Send(){
        byte val=0;
        if(state[0] == 1)val += 1;
        if(state[1] == 1)val += 2;
        if(state[2] == 1)val += 4;
        if(state[3] == 1)val += 8;

        data[0] = val;
        connection.bulkTransfer(epOUT, data, data.length, 0);
        Log.d("arduino", "data:"+Integer.toString(val));
    }
}
