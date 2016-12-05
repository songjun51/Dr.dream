package kr.edcan.drdream;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MainActivity extends AppCompatActivity {
    DataManager manager;
    boolean isStarted = false;
    BluetoothSPP bt;
    TextView temp1, temp2, huni1, huni2, time;
    ImageView imtemp, imhuni, imtime;
    String[] str = {"", "", "", ""};
    int temp;
    int huni;
    String slptime;
    boolean shit = true;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefault();
        setClick();
        init();


        bt = new BluetoothSPP(this);

        if (!bt.isBluetoothAvailable())

        {
            Toast.makeText(getApplicationContext()
                    , "블루투스를 켜주세요"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener()

        {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "연결되었습니다", Toast.LENGTH_SHORT).show();

                imtemp.setImageResource(R.drawable.ic_snow);
                imhuni.setImageResource(R.drawable.ic_opacity);
                imtime.setImageResource(R.drawable.ic_opacity);


            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "연결이끊겼습니다"
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
            }
        });

        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
            }

            public void onAutoConnectionStarted() {
            }
        });


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                str = message.split(".");

                // 0 flex, 1 temp, 2 humi, 3 light
                if (message.length() >= 8) {
                    huni1.setText(message.substring(0, 2));
                    temp1.setText(message.substring(6, 8));
                }
                msg = message;
                Log.e("asdf", message);
                String nowtime;
                nowtime = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
                Log.e("asdf", nowtime);
                if (nowtime.equals(slptime)) {
                    if (shit = true) {
                        Log.e("asdf", ":알람");
                        bt.send("a", true);//티비끄기

//                        while (!message.equals("getA")) {
                        if(message.equals("getA")){
                            Log.e("asdf","GetA기다리기");
                            shit = false;
                        }
                    }
                } else
                    Log.e("asdf", "알람ㅅ기간아님");
                shit = true;

            }
        });

    }

    private void setDefault() {
        manager = new DataManager(getApplicationContext());

    }

    private void init() {
        temp1 = (TextView) findViewById(R.id.nowtemp);
        temp2 = (TextView) findViewById(R.id.retemp);

        huni1 = (TextView) findViewById(R.id.nowhumi);
        huni2 = (TextView) findViewById(R.id.rehumi);

        time = (TextView) findViewById(R.id.sleepTime);

        imtemp = (ImageView) findViewById(R.id.IV_temp);
        imhuni = (ImageView) findViewById(R.id.IV_humi);
        imtime = (ImageView) findViewById(R.id.IV_time);


//        (findViewById(R.id.noHumidVisible)).setVisibility((manager.getHumid() == -1) ? View.VISIBLE : View.GONE);
//        (findViewById(R.id.humidVisible)).setVisibility((manager.getHumid() == -1) ? View.GONE : View.VISIBLE);
//        (findViewById(R.id.noTempVisible)).setVisibility((manager.getTemp() == -1) ? View.VISIBLE : View.GONE);
//        (findViewById(R.id.tempVisible)).setVisibility((manager.getTemp() == -1) ? View.GONE : View.VISIBLE);
        if (manager.getTemp() != -1) {
            ((TextView) findViewById(R.id.retemp)).setText(manager.getTemp() + "");
        }
        if (manager.getHumid() != -1) {
            ((TextView) findViewById(R.id.rehumi)).setText(manager.getHumid() + "");
        }
    }

    private void setClick() {
        (findViewById(R.id.settingHumid)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View origin = getLayoutInflater().inflate(R.layout.edittext_inflate, null);
                new MaterialDialog.Builder(MainActivity.this)
                        .title("습도를 설정해주세요!")
                        .customView(origin, true)
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                manager.setHumid(Integer.parseInt(((EditText) origin.findViewById(R.id.input)).getText().toString()));
                                init();
                                String fuck;
                                fuck = ((EditText) origin.findViewById(R.id.input)).getText().toString();
                                huni = Integer.parseInt(fuck);
                            }
                        })
                        .show();
            }
        });
        (findViewById(R.id.settingTemp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View origin = getLayoutInflater().inflate(R.layout.edittext_inflate, null);
                new MaterialDialog.Builder(MainActivity.this)
                        .title("온도를 설정해주세요!")
                        .customView(origin, true)
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                manager.setTemp(Integer.parseInt(((EditText) origin.findViewById(R.id.input)).getText().toString()));
                                init();
                                String fuck;
                                fuck = ((EditText) origin.findViewById(R.id.input)).getText().toString();
                                temp = Integer.parseInt(fuck);

                            }
                        })
                        .show();
            }
        });
        (findViewById(R.id.settingSleepTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View origin = getLayoutInflater().inflate(R.layout.edittext_inflate, null);
                new MaterialDialog.Builder(MainActivity.this)
                        .title("수면 시간을 설정해주세요!")
                        .customView(origin, true)
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ((TextView) (findViewById(R.id.sleepTime))).setText(((EditText) origin.findViewById(R.id.input)).getText().toString());
                                init();
                                slptime = ((EditText) origin.findViewById(R.id.input)).getText().toString();
//                                slptime=Integer.parseInt(fuck);

                            }
                        })
                        .show();
            }
        });
        (findViewById(R.id.startSleep)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "수면이 시작되었습니다!", Toast.LENGTH_SHORT).show();
                Log.e("asdf", "slptime : " + slptime);
            }
        });
        //티비끄기
//        bt.send("a",true);

    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "블루투스 켜주세요"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void setup() {
        bt.autoConnect("DR.dream");
    }

}

