package com.rm.easy.crm.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.rm.easy.crm.R;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;
import com.rm.easy.crm.util.ValidatorUtil;

import java.util.Timer;
import java.util.TimerTask;

public class CreateClient extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final int CREATE_SUCCESS = 1;
    private static final int CREATE_FAIL = 2;
    public static final int CONNECT_FAIL = 4;

    //初始化控件
    private EditText clientName;
    private EditText clientPhone;
    private EditText clientAdd;

    private RadioGroup clientSexRadioGroup;

    private Button submit;


    private String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);
        clientName = (EditText) findViewById(R.id.clientname_edit);
        clientPhone = (EditText) findViewById(R.id.clientphone_edit);
        clientAdd = (EditText) findViewById(R.id.clientadd_edit);

        clientSexRadioGroup = (RadioGroup) findViewById(R.id.clientsex_radiogroup);

        clientSexRadioGroup.setOnCheckedChangeListener(this);

        submit = (Button) findViewById(R.id.client_submit);
        submit.setOnClickListener(this);

    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CREATE_SUCCESS:
                    Toast.makeText(CreateClient.this, "保存成功", Toast.LENGTH_SHORT).show();
                    break;
                case CREATE_FAIL:
                    Toast.makeText(CreateClient.this, "保存失败", Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAIL:
                    Toast.makeText(CreateClient.this, "建立连接中，请稍候", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.i("CreateClient", "Reconnecting");
                            String reqStr = "clientName=" + clientName.getText() + "&clientPhone=" + clientPhone.getText() + "&clientAdd=" + clientAdd.getText() + "&clientSex=" + getSex();
                            String address = "http://zkcoffee.imwork.net/client/create_client.php";
                            sendRequest(address, reqStr);
                            this.cancel();
                        }
                    }, 500);
                    break;
            }
        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.client_submit:
                Log.i("CreateClient", clientName.getText().toString());
                if (clientName.getText().toString().equals("") || clientPhone.getText().toString().equals("") || getSex() == null) {
                    Toast.makeText(CreateClient.this, "请输入客户姓名、电话和性别", Toast.LENGTH_SHORT).show();
                } else {
                    if (ValidatorUtil.isMobile(clientPhone.getText().toString())) {
                        String str = "clientName=" + clientName.getText() + "&clientPhone=" + clientPhone.getText() + "&clientAdd=" + clientAdd.getText() + "&clientSex=" + getSex();
                        String address = "http://zkcoffee.imwork.net/client/create_client.php";
                        sendRequest(address, str);
                    } else {
                        Toast.makeText(CreateClient.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        setSex(rb.getText().toString());

    }

    private void sendRequest(String address, String str) {
        HttpUtil.sendHttpRequest(address, str, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("CreateClient", response);
                JsonGeneral jsonGeneral = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                Log.i("CreateClient", jsonGeneral.getStatus());
                Message msg = new Message();
                if (jsonGeneral.getStatus().equals("Success")) {
                    msg.what = CREATE_SUCCESS;
                } else {
                    msg.what = CREATE_FAIL;
                }
                msg.obj = jsonGeneral.getStatus();
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
