package com.example.zhangwenqiang.rjks_final_pro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ListView extends Activity {
    public String data;
    public int [] TransCards = new int[17];
    Context context;
    Button Return;

    Button.OnClickListener mReturnListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(ListView.this,MainActivity.class);
                    intent.putExtra("extra_data",data);
                    intent.putExtra("sig",1);
                    startActivity(intent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int num = intent.getIntExtra("index",-1);
        String newcard = intent.getStringExtra("extra_data");
        data = intent.getStringExtra("initial");
        setContentView(R.layout.activity_listview);
        // 绑定Layout里面的ListView
        android.widget.ListView list = (android.widget.ListView) findViewById(R.id.ListView);
        Return = (Button) findViewById(R.id.btn_0);
        Return.setOnClickListener(mReturnListener);

        context = this;
        int row; //行
        int col; //列
        if(newcard.split(",").length == 17)
            data = newcard;
        else if(num != -1) {
            String[] arr = data.split(",");
            arr[num] = newcard;
            data = "";
            for(int i =0;i<16;i++)
                data = data + arr[i] +",";
            data = data + arr[16];
        }
        String[] arr = data.split(",");
        /*if(num!=-1) {
            arr[num] = newcard;
            data = "";
            for (int i = 0; i < 16; i++)
                data = data + arr[i];
            data = data + arr[16];
        }*/
        int ji;
        for(int i = 0; i<17; i++)
        {
            String tem0 = arr[i].substring(0,1);
            String tem1 = arr[i].substring(1,2);
            if(tem0.equals("J"))
                ji = 8;
            else if(tem0.equals("Q"))
                ji = 9;
            else if(tem0.equals("K"))
                ji = 10;
            else if(tem0.equals("A"))
                ji = 11;
            else if(tem0.equals("2"))
                ji = 12;
            else if(tem1.equals("0"))
            {
                ji = 7;
                tem1 = arr[i].substring(2,3);
            }
            else
                ji = Integer.parseInt(tem0) - 3;
            switch (tem1){
                case "s":
                    TransCards[i] = ji*4;
                    break;
                case "h":
                    TransCards[i] = ji*4 + 1;
                    break;
                case "c":
                    TransCards[i] = ji*4 + 2;
                    break;
                case "d":
                    TransCards[i] = ji*4 + 3;
                    break;
            }
        }

        // 生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 17; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            row = TransCards[i]/4;
            col = TransCards[i]%4;
            map.put("ItemImage",CardImage.cardImages[row][col]);// 图像资源的ID
            map.put("ItemTitle", "Card" + i);
            map.put("ItemText", "This is the card you have photoed.");
            listItem.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
                R.layout.list_items,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[] { "ItemImage", "ItemTitle", "ItemText" },
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText });

        // 添加并且显示
        list.setAdapter(listItemAdapter);

        // 添加点击
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ListView.this,seletecards.class);
                intent.putExtra("index",arg2);
                intent.putExtra("extra_data",data);
                startActivity(intent);
            }
        });

        // 添加长按点击
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("长按菜单-ContextMenu");
                menu.add(0, 0, 0, "弹出长按菜单0");
                menu.add(0, 1, 0, "弹出长按菜单1");
            }
        });
    }

    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        setTitle("点击了长按菜单的第" + item.getItemId() + "项");
        return super.onContextItemSelected(item);
    }
}
