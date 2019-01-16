package com.example.zhangwenqiang.rjks_final_pro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class seletecards extends AppCompatActivity {
    String final_num;
    String final_color;
    public String data_ = "";
    private static final String[] ANDROID_VERSIONS = {
            "A",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "J",
            "Q",
            "K"
    };
    private static final String[] colors = {
            "红桃",
            "黑桃",
            "方块",
            "梅花"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final int da = intent.getIntExtra("index",-1);
        final String dat = intent.getStringExtra("extra_data");
        setContentView(R.layout.activity_seletecards);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems(ANDROID_VERSIONS);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                final_num = item;
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        MaterialSpinner spinner2 = (MaterialSpinner) findViewById(R.id.spinner2);
        spinner2.setItems(colors);
        spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                switch(item){
                    case "红桃":
                        final_color = "h";
                        break; //可选
                    case "黑桃":
                        final_color = "s";
                        break; //可选
                    case "方块":
                        final_color = "d";
                        break; //可选
                    case "梅花":
                        final_color = "c";
                        break; //可选
                    //你可以有任意数量的case语句
                    default : //可选
                        final_color = "c";
                }
            }
        });

        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seletecards.this,ListView.class);
                if(!final_color.isEmpty())
                {
                    if(!final_num.isEmpty())
                    {
                        data_ = final_num + final_color;
                        intent.putExtra("extra_data",data_);
                        intent.putExtra("initial",dat);
                        intent.putExtra("index",da);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
