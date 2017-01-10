package com.rm.easy.crm.warehouse;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.rm.easy.crm.R;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;

public class AddItem extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //初始化常量

    private static final String HOST = "http://rmcoffee.imwork.net/";
    private static final String TAG = "AddItem";
    public static final int CONNECT_FAIL = 0;

    //控件初始化
    private Spinner selectWarehouse;
    private Spinner selectItem;
    private TextView inventory;
    private EditText addInventory;
    private Button addSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        selectWarehouse = (Spinner)findViewById(R.id.add_item_select_warehouse);
        selectItem = (Spinner)findViewById(R.id.add_item_select_item);
        inventory = (TextView)findViewById(R.id.additem_have_weight);
        addInventory = (EditText)findViewById(R.id.additem_add_weight);
        addSubmit = (Button)findViewById(R.id.additem_submit);

        //设置控件监听器

        selectWarehouse.setOnItemSelectedListener(this);
        selectItem.setOnItemSelectedListener(this);
        addSubmit.setOnClickListener(this);
    }
    //Handler

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){

            }
        }
    };


    //HTTP

    private void sendRequest(String address, String str){
        HttpUtil.sendHttpRequest(address, str, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i(TAG,response);
                JsonGeneral jsonGenerals = GsonUtil.parseJsonWithGson(response, JsonGeneral.class);
                Log.i(TAG, jsonGenerals.getStatus());


            }

            @Override
            public void onError(Exception e) {
                Message msg = new Message();
                msg.what = CONNECT_FAIL;
                handler.sendMessage(msg);
                e.printStackTrace();

            }
        });

    }


    //响应按钮事件

    @Override
    public void onClick(View view) {

    }


    //相应Spinner选择事件
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
