package com.rm.easy.crm.warehouse;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.rm.easy.crm.R;
import com.rm.easy.crm.adapter.SelectWarehouseAdapter;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class CreateItem extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {


    private static final int WAREHOUSE_INIT_SUCCESS = 0;
    private static final int WAREHOUSE_INIT_FAIL = 1;
    private static final int CREATE_ITEM_SUCCESS = 3;
    private static final int CREATE_ITEM_FAIL = 4;

    //Init widget

    private EditText itemName;
    private EditText itemWeight;

    private RadioGroup weightUnitRadioGroup;

    private Spinner selectWarehouse;

    private Button createItemSubmit;

    private List<String> warehouseList = new ArrayList();



    private JsonGeneral jG;
    public String weight;


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
        weightUnitRadioGroup.setOnCheckedChangeListener(this);
        selectWarehouse.setOnItemSelectedListener(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WAREHOUSE_INIT_SUCCESS:
                    SelectWarehouseAdapter arr_adapter = new SelectWarehouseAdapter(CreateItem.this, android.R.layout.simple_spinner_item, warehouseList);
                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectWarehouse.setAdapter(arr_adapter);
                    break;
                case WAREHOUSE_INIT_FAIL:
                    Toast.makeText(CreateItem.this, "获取仓库信息失败", 500).show();
                    break;
                case CREATE_ITEM_SUCCESS:
                    Toast.makeText(CreateItem.this, "保存成功", Toast.LENGTH_SHORT).show();
                    break;
                case CREATE_ITEM_FAIL:
                    Toast.makeText(CreateItem.this, "保存失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.create_item_submit:
                Log.i("CreateItem","Click Submit");
                //调用sendRequest()发送保存数据请求
                break;
            default:
                break;
        }

    }

    //Spinner 接口实例化
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(CreateItem.this, warehouseList.get(i).toString(), 500).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //RadioGroup 接口实例化
    @Override
    public void onCheckedChanged(RadioGroup radioGroup,  int i) {
        RadioButton rb = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        setWeight(rb.getTag().toString());
        Log.i("CreateItem", "U Click: "+getWeight());
    }

    private void getResponse(String address){
        HttpUtil.getHttpResponse(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("CreateItem",response);
                JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                setjG(jsonGenerals);
                Log.i("CreateItem",jsonGenerals.getStatus());
                while(jsonGenerals.getData().iterator().hasNext()){
                    warehouseList.add(jsonGenerals.getData().get(0).getWarehouseName().toString());
                    jsonGenerals.getData().remove(0);
                }
                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")) {
                    msg.what = WAREHOUSE_INIT_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = WAREHOUSE_INIT_FAIL;
                }
                handler.sendMessage(msg);

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void sendRequest(String address, String str){
        HttpUtil.sendHttpRequest(address, str, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("CreateItem",response);
                JsonGeneral jsonGenerals = GsonUtil.parseJsonWithGson(response, JsonGeneral.class);
                Log.i("CreateItem", jsonGenerals.getStatus());
                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")){
                    msg.what = CREATE_ITEM_SUCCESS;
                }else{
                    msg.what = CREATE_ITEM_FAIL;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    //Getter&Setter
    public JsonGeneral getjG() {
        return jG;
    }

    public void setjG(JsonGeneral jG) {
        this.jG = jG;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
