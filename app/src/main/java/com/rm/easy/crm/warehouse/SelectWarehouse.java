package com.rm.easy.crm.warehouse;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.rm.easy.crm.R;
import com.rm.easy.crm.adapter.SelectInventoryAdapter;
import com.rm.easy.crm.adapter.SelectWarehouseAdapter;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectWarehouse extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final int WAREHOUSE_INIT_FAIL = 0;
    public static final int WAREHOUSE_INIT_SUCCESS = 1;
    public static final int SELECT_INVENTORY_FAIL = 2;
    public static final int SELECT_INVENTORY_SUCCESS = 3;

    public static final String TAG = "SelectWarehouse";

    private static final String HOST = "http://rmcoffee.imwork.net/";


    //Init Widget

    private Button selectInventory;
    private ListView inventoryList;
    private Spinner selectWarehouse;


    private List<String> warehouseList = new ArrayList();

    private String warehouseName;
    private JsonGeneral jG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_warehouse);

        getResponse(HOST + "warehouse/select_warehouse.php");


        selectInventory = (Button)findViewById(R.id.get_select_warehouse);
        selectWarehouse = (Spinner)findViewById(R.id.select_warehouse_spinner);
        inventoryList = (ListView)findViewById(R.id.listview_select_invnetory);

        selectInventory.setOnClickListener(this);
        selectWarehouse.setOnItemSelectedListener(this);

    }

    //Handler
    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case WAREHOUSE_INIT_SUCCESS:
                    SelectWarehouseAdapter adapter = new SelectWarehouseAdapter(SelectWarehouse.this, android.R.layout.simple_spinner_item, warehouseList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectWarehouse.setAdapter(adapter);
                    break;
                case WAREHOUSE_INIT_FAIL:
                    Toast.makeText(SelectWarehouse.this, "数据初始化失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case SELECT_INVENTORY_FAIL:
                    Toast.makeText(SelectWarehouse.this, "可能是网络连接失败，你要不再试试", Toast.LENGTH_SHORT).show();
                    break;
                case SELECT_INVENTORY_SUCCESS:
                    SelectInventoryAdapter inventoryAdapter = new SelectInventoryAdapter(SelectWarehouse.this, R.layout.select_inventory_adapter, getjG().getData());
                    inventoryList.setAdapter(inventoryAdapter);

                    break;
            }
        }

    };



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.get_select_warehouse:
                String address = HOST + "warehouse/select_inventory.php";
                String parameter = "warehouseItemWarehouseName=" + getWarehouseName();
                Log.i(TAG,getWarehouseName());
                sendRequest(address,parameter);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(SelectWarehouse.this, warehouseList.get(i).toString(), 500).show();
        setWarehouseName(warehouseList.get(i).toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Http Request&Response

    private void getResponse(String address){
        HttpUtil.getHttpResponse(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i(TAG, response);
                JsonGeneral jsonGenerals = GsonUtil.parseJsonWithGson(response, JsonGeneral.class);
                Log.i(TAG,jsonGenerals.getStatus());
                Message msg = new Message();
                //此处判断服务器的响应，成功返回Success，失败返回Fail
                if (jsonGenerals.getStatus().equals("Success")) {
                    while(jsonGenerals.getData().iterator().hasNext()){
                        warehouseList.add(jsonGenerals.getData().get(0).getWarehouseName().toString());
                        jsonGenerals.getData().remove(0);
                    }
                    msg.what = WAREHOUSE_INIT_SUCCESS;

                } else {
                    msg.what = WAREHOUSE_INIT_FAIL;
                }
                msg.obj = jsonGenerals.getStatus();
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Message msg = new Message();
                msg.what = WAREHOUSE_INIT_FAIL;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        });
    }

    private void sendRequest(String address, String parameter){
        HttpUtil.sendHttpRequest(address, parameter, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i(TAG, response);
                JsonGeneral jsonGenerals = GsonUtil.parseJsonWithGson(response,JsonGeneral.class);
                Log.i(TAG,jsonGenerals.getStatus());
                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")){
                    msg.what = SELECT_INVENTORY_SUCCESS;
                    setjG(jsonGenerals);
                }else{
                    msg.what = SELECT_INVENTORY_FAIL;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Message msg = new Message();
                msg.what = SELECT_INVENTORY_FAIL;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        });
    }



    //Getter & Setter

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public JsonGeneral getjG() {
        return jG;
    }

    public void setjG(JsonGeneral jG) {
        this.jG = jG;
    }
}
