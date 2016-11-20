package com.rm.easy.crm.client;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class SelectClient extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static final int SELECT_SUCCESS = 0;
    private static final int SELECT_FAIL = 1;
    private static final int CONNECT_FAIL = 2;
    private static final int DELETE_SUCCESS = 3;
    private static final int DELETE_FAIL = 4;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    private String clientId;

    public JsonGeneral getJg() {
        return jg;
    }

    public void setJg(JsonGeneral jg) {
        this.jg = jg;
    }

    private JsonGeneral jg;


    //初始化控件
    private Button getClientList;

    private ListView clientListView;

    private List<Data> datalist = new ArrayList<Data>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);
        getClientList = (Button) findViewById(R.id.get_select_client);

        clientListView = (ListView) findViewById(R.id.listview_select_client);

        getClientList.setOnClickListener(this);
        clientListView.setOnItemClickListener(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECT_SUCCESS:
                    Toast.makeText(SelectClient.this, "查询成功", Toast.LENGTH_SHORT).show();
                    SelectClientAdapter adapter = new SelectClientAdapter(SelectClient.this, R.layout.select_client_listview_item, getJg().getData());
                    clientListView.setAdapter(adapter);
                    break;
                case SELECT_FAIL:
                    Toast.makeText(SelectClient.this, "查询失败", Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAIL:
                    Toast.makeText(SelectClient.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case DELETE_SUCCESS:
                    Toast.makeText(SelectClient.this, "删除成功", Toast.LENGTH_SHORT).show();
                    String add = "http://rmcoffee.imwork.net/client/select_client.php";
                    getResponse(add);
                    break;
                case DELETE_FAIL:
                    Toast.makeText(SelectClient.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_select_client:
                String add = "http://rmcoffee.imwork.net/client/select_client.php";
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
                setJg(jsonGenerals);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String [] str = {"删除"};
        final Data data = getJg().getData().get(i);
        setClientId(data.getId());
        Log.i("SelectClient", "Click ListView Item: " + data.getClientName());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择操作(谨慎！)");
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                Log.i("SelectClient", data.getClientId());
                String address = "http://rmcoffee.imwork.net/client/delete_client.php";
                String str = "clientId=" + data.getClientId();
                sendRequest(address, str);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void sendRequest(String address, String str){
        HttpUtil.sendHttpRequest(address, str, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("SelectClient", response);
                JsonGeneral jsonGeneralsSend = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                Log.i("SelectClient", jsonGeneralsSend.getStatus());
                Message msg = new Message();
                if (jsonGeneralsSend.getStatus().equals("Success")) {
                    msg.what = DELETE_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = DELETE_FAIL;
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
