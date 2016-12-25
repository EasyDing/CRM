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
    private static final int CONNECT_FAIL = 5;
    private static final int CHECK_ITEM_OK = 6; //数据库中存在
    private static final int CHECK_ITEM_ERROR = 7; //数据库中不存在，允许插入数据
    private static final String HOST = "http://rmcoffee.imwork.net/";

    //Init widget

    private EditText itemName;
    private EditText itemWeight;

    private RadioGroup weightUnitRadioGroup;

    private Spinner selectWarehouse;

    private Button createItemSubmit;

    private List<String> warehouseList = new ArrayList();



    private JsonGeneral jG;
    private Float weight;
    private String warehouseName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        getResponse(HOST+"warehouse/select_warehouse.php",WAREHOUSE_INIT_SUCCESS,WAREHOUSE_INIT_FAIL);
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
                case CONNECT_FAIL:
                    Toast.makeText(CreateItem.this, "连接超时，请手动重试", Toast.LENGTH_SHORT).show();
                    break;
                case CHECK_ITEM_OK:
                    Log.i("CreateITem",msg.obj.toString());
                    Toast.makeText(CreateItem.this, "数据已存在", Toast.LENGTH_SHORT).show();
                    break;
                case CHECK_ITEM_ERROR:
                    Log.i("CreateITem",msg.obj.toString());
                    //调用sendRequest()发送保存数据请求
                    String str = "warehouseItemName=" + itemName.getText() + "&warehouseItemWeight=" + getWeight() + "&warehouseItemWarehouseName=" + getWarehouseName();
                    String address = HOST + "warehouse/create_item.php";
                    sendRequest(address,str,CREATE_ITEM_SUCCESS,CREATE_ITEM_FAIL);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.create_item_submit:
                Log.i("CreateItem","Click Submit");
                //检查数据库中是否存在相同数据
                String checkStr = "warehouseItemName=" + itemName.getText() + "&warehouseItemWarehouseName=" + getWarehouseName();
                String checkAddress = HOST +"warehouse/check_item.php";
                sendRequest(checkAddress,checkStr, CHECK_ITEM_OK, CHECK_ITEM_ERROR);


                break;
            default:
                break;
        }

    }

    //Spinner 接口实例化
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(CreateItem.this, warehouseList.get(i).toString(), 500).show();
        setWarehouseName(warehouseList.get(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //RadioGroup 接口实例化
    @Override
    public void onCheckedChanged(RadioGroup radioGroup,  int i) {
        if (itemName.getText().toString().equals("") || itemWeight.getText().toString().equals("")){

            Toast.makeText(CreateItem.this, "请输入品类", Toast.LENGTH_SHORT).show();
        }else{

            RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            //预处理重量单位，保存全部以kg进行保存
            switch (rb.getTag().toString()){
                case "g":
                    setWeight(Float.parseFloat(itemWeight.getText().toString())/1000);
                    break;
                case "t":
                    setWeight(Float.parseFloat(itemWeight.getText().toString())*1000);
                    break;
                case "kg":
                    setWeight(Float.parseFloat(itemWeight.getText().toString()));
                    break;
            }
            Log.i("CreateItem", "U Click: " + getWeight().toString());
        }
    }

    private void getResponse(String address, final int success, final int fail){
        HttpUtil.getHttpResponse(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("CreateItem",response);
                JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                setjG(jsonGenerals);
                Log.i("CreateItem",jsonGenerals.getStatus());

                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")) {
                    while(jsonGenerals.getData().iterator().hasNext()){
                        warehouseList.add(jsonGenerals.getData().get(0).getWarehouseName().toString());
                        jsonGenerals.getData().remove(0);
                    }
                    msg.what = success;
                    msg.obj = response;
                } else {
                    msg.what = fail;
                    msg.obj = response;
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

    private void sendRequest(String address, String str, final int success, final int fail){
        HttpUtil.sendHttpRequest(address, str, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("CreateItem",response);
                JsonGeneral jsonGenerals = GsonUtil.parseJsonWithGson(response, JsonGeneral.class);
                Log.i("CreateItem", jsonGenerals.getStatus());
                Message msg = new Message();
                if (jsonGenerals.getStatus().equals("Success")){
                    msg.what = success;
                    msg.obj = response;
                }else{
                    msg.what = fail;
                    msg.obj = response;
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


    //Getter&Setter
    public JsonGeneral getjG() {
        return jG;
    }

    public void setjG(JsonGeneral jG) {
        this.jG = jG;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
