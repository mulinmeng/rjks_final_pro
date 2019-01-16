package com.example.zhangwenqiang.rjks_final_pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.roger.catloadinglibrary.CatLoadingView;

public class GalleryActivity extends Activity {
    private static final String[] ANDROID_VERSIONS = {
            "Cupcake",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow",
            "Nougat",
            "Oreo"
    };
    CatLoadingView mView;

    //    String data = "6h,2r,4d,3h,3h";
//    TextView tvFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
////        tvFileName = (TextView) findViewById(R.id.tvHeading);
//        Intent intent = getIntent();
//        final String data = intent.getStringExtra("extra_data");
////        tvFileName.setText(data);
//
//        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
//        spinner.setItems(ANDROID_VERSIONS);
//        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make(view, data, Snackbar.LENGTH_LONG).show();
//            }
//        });
//        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
//
//            @Override public void onNothingSelected(MaterialSpinner spinner) {
//                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
//            }
//        });
//
//        Button b = (Button) findViewById(R.id.button2);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GalleryActivity.this,seletecards.class);
//                intent.putExtra("extra_data",data);
//                startActivity(intent);
//            }
//        });
//    }
    }
}
