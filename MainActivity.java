package com.example.zhangwenqiang.rjks_final_pro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.content.Intent;

import java.io.File;


@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

    public final static int MENU = 0;
    public final static int GAME = 1;
    public final static int EXIT = 2;
    public final static int SMALL_CARD = 3;
    public final static int WRONG_CARD = 4;
    public final static int EMPTY_CARD = 5;
    public final static int START = 6;
    public final static int CONTINUE = 7;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static double SCALE_VERTICAL;
    public static double SCALE_HORIAONTAL;
    public static Handler handler;
    private MenuView mv;
    private GameView gv;

    private File tempFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String da = intent.getStringExtra("extra_data");


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_HEIGHT > SCREEN_WIDTH) {
            int temp = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = temp;
        }
        System.out.println(SCREEN_HEIGHT + "X" + SCREEN_WIDTH);
        SCALE_VERTICAL = SCREEN_HEIGHT / 320.0;
        SCALE_HORIAONTAL = SCREEN_WIDTH / 480.0;
        System.out.println(SCALE_VERTICAL + " and " + SCALE_HORIAONTAL);

        mv = new MenuView(this);
        gv = new GameView(this.getApplicationContext(),da);
        setContentView(mv);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 0 :
                        setContentView(mv);
                        break;
                    case 1 :
                    {
                        //setContentView(gv);
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2 :
                        finish();
                        break;
                    case 3 :
                        Toast.makeText(getApplicationContext(), "你的牌太小！", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 4 :
                        Toast.makeText(getApplicationContext(), "出牌不符合规则！", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 5 :
                        Toast.makeText(getApplicationContext(), "请出牌！", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:{
                        Intent intent = getIntent();
                        int sign = intent.getIntExtra("sig",0);
                        if(sign == 1)
                            setContentView(gv);
                        else
                            Toast.makeText(getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 7:{
                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }
                }
            }

        };

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                super.onResume();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
