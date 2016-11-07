package com.rm.easy.crm;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.rm.easy.crm.bean.JsonGeneral;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;

import static android.view.View.VISIBLE;


public class UserLogin extends Activity implements View.OnClickListener {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAIL = 1;

    private EditText userName;
    private EditText userPwd;

    private Button orderManage;
    private Button warehouseManage;
    private Button clientManage;
    private Button financingManage;
    private Button sendMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userName = (EditText) findViewById(R.id.uname);
        userPwd = (EditText) findViewById(R.id.upass);
        orderManage = (Button) findViewById(R.id.order);
        warehouseManage = (Button) findViewById(R.id.warehouse);
        clientManage = (Button) findViewById(R.id.client);
        financingManage = (Button) findViewById(R.id.financing);
        sendMsg = (Button) findViewById(R.id.submit);
        sendMsg.setOnClickListener(this);
    }


    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case LOGIN_SUCCESS:
                    //登录成功显示管理按钮
                    String successRes = (String) msg.obj;
                    orderManage.setVisibility(VISIBLE);
                    warehouseManage.setVisibility(VISIBLE);
                    clientManage.setVisibility(VISIBLE);
                    financingManage.setVisibility(VISIBLE);
                    Toast.makeText(UserLogin.this, successRes, Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAIL:
                    String failRes = (String) msg.obj;
                    Toast.makeText(UserLogin.this, failRes, Toast.LENGTH_SHORT).show();
            }
        }

    };

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.submit){
            String address = "http://192.168.31.218/www/user/select_user.php";
            String reqStr = "uname=" + userName.getText() + "&upwd=" + userPwd.getText();
            sendRequest(address, reqStr);
        }
    }

    private void sendRequest(String address, String reqStr){
       HttpUtil.sendHttpRequest(address, reqStr, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                Log.i("jsonGenerals", jsonGenerals.getStatus());
                Message msg = new Message();
                //此处判断服务器的响应，成功返回Success，失败返回Fail
                if(jsonGenerals.getStatus().equals("Success")) {
                    msg.what = LOGIN_SUCCESS;
                }else{
                    msg.what = LOGIN_FAIL;
                }
                msg.obj = jsonGenerals.getStatus();
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {

                e.printStackTrace();

            }
        });
    }
}
