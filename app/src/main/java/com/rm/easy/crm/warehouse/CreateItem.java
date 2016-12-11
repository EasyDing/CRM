package com.rm.easy.crm.warehouse;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.rm.easy.crm.R;
import com.rm.easy.crm.adapter.SelectClientAdapter;
import com.rm.easy.crm.adapter.SelectWarehouseAdapter;
import com.rm.easy.crm.client.SelectClient;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class CreateItem extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final int SELECT_SUCCESS = 0;
    private static final int SELECT_FAIL = 1;

    //Init widget

    private EditText itemName;
    private EditText itemWeight;

    private RadioGroup weightUnitRadioGroup;

    private Spinner selectWarehouse;

    private Button createItemSubmit;

    private List<String> warehouseList = new ArrayList();

    public JsonGeneral getjG() {
        return jG;
    }

    public void setjG(JsonGeneral jG) {
        this.jG = jG;
    }

    private JsonGeneral jG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        getResponse("http://rmcoffee.imwork.net/warehouse/select_warehouse.php");
        itemName = (EditText)findViewById(R.id.create_item_name_edit);
        itemWeight = (EditText)findViewById(R.id.create_item_inventory_edit);
        weightUnitRadioGroup = (RadioGroup)findViewById(R.id.weight_unit_radiogroup);
        selectWarehouse = (Spinner)findViewById(R.id.create_item_select_warehouse);
        createItemSubmit = (Button)findViewById(R.id.create_item_submit);

        createItemSubmit.setOnClickListener(this);



        selectWarehouse.setOnItemSelectedListener(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SELECT_SUCCESS:
                    SelectWarehouseAdapter arr_adapter = new SelectWarehouseAdapter(CreateItem.this, android.R.layout.simple_spinner_item, getjG().getData());
                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectWarehouse.setAdapter(arr_adapter);
                    break;

            }
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.create_item_submit:
                Log.i("CreateItem","Click Submit");

                break;
            default:
                break;
        }

    }

    //Spinner 接口实例化


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(CreateItem.this, getjG().getData().get(i).getWarehouseName(), 500).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getResponse(String address){
        HttpUtil.getHttpResponse(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("CreateItem",response);
                JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                setjG(jsonGenerals);
                Log.i("CreateItem",jsonGenerals.getStatus());
                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")) {
                    msg.what = SELECT_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = SELECT_FAIL;
                }
                handler.sendMessage(msg);
//                while(jsonGeneral.getData().iterator().hasNext()){
//                    warehouseList.add(jsonGeneral.getData().get(0).getWarehouseName().toString());
//                    jsonGeneral.getData().remove(0);
//                }

//                final int size = warehouseList.size();
//                warehouseArray = (String[])warehouseList.toArray();

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
