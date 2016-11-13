package com.rm.easy.crm.client;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.rm.easy.crm.R;
import com.rm.easy.crm.jsonBean.Data;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.listviewAdapter.SelectClientAdapter;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectClient extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_SUCCESS = 0;
    private static final int SELECT_FAIL = 1;
    private static final int CONNECT_FAIL = 2;


    //初始化控件
    private Button getClientList;

    private ListView clientListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);
        getClientList = (Button) findViewById(R.id.get_select_client);

        clientListView = (ListView) findViewById(R.id.listview_select_client);

        getClientList.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECT_SUCCESS:
                    Toast.makeText(SelectClient.this, "查询成功", Toast.LENGTH_SHORT).show();
                    JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(msg.obj.toString(), JsonGeneral.class);
                    SelectClientAdapter adapter = new SelectClientAdapter(SelectClient.this, R.layout.select_client_listview_item, jsonGenerals.getData());
                    clientListView.setAdapter(adapter);
                    break;
                case SELECT_FAIL:
                    Toast.makeText(SelectClient.this, "查询失败", Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAIL:
                    Toast.makeText(SelectClient.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_select_client:
                String add = "http://zkcoffee.imwork.net/client/select_client.php";
                getResponse(add);
                break;
        }
    }

    private void getResponse(String address) {
        HttpUtil.getHttpResponse(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("SelectClient", response);
                JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                Log.i("SelectClient", jsonGenerals.getStatus());
                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")) {
                    msg.what = SELECT_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = SELECT_FAIL;
                }
                handler.sendMessage(msg);

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
}
