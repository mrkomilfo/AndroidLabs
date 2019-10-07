package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showVersion();
    }

    public void onButtonClick(View view)
    {
        showImei();
    }

    public void explanation(){
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Lack of permissions!")
                .setMessage("We need access to your phone state to know your IMEI.")
                .setCancelable(false)
                .setNegativeButton("Got it",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                handler.sendMessage(handler.obtainMessage());
                                dialog.cancel();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
        try{ Looper.loop(); }
        catch(RuntimeException e){}
    }

    protected void showVersion() {
        TextView version = (TextView) findViewById(R.id.versionLabel);
        version.setText(BuildConfig.VERSION_NAME);
    }

    protected void showImei() {
        TextView imei = (TextView) findViewById(R.id.imeiLabel);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            imei.setText(deviceId);
        }
        else
        {
            explanation();
            imei.setText("unknown");
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
        }
    }

    @Override
    public  void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showImei();
            }
            else
            {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
