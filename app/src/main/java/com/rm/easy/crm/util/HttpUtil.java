package com.rm.easy.crm.util;

import com.rm.easy.crm.iface.HttpCallbackListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Easy.D on 2016/11/6.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final String reqStr, final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //使用write(str.getBytes())替换writeBytes(str)解决中文乱码问题，out.writeBytes(str);该语句在转中文时候，已经变成乱码
                    //所以在可能有中文字符输出的地方最好先将其转换为字节数组，然后再通过write写入流
                    out.write(reqStr.getBytes());
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(5000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder res = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        res.append(line);
                    }
                    if(listener != null){
                        listener.onFinish(res.toString());
                    }
                } catch (Exception e){
                    if(listener != null){
                        listener.onError(e);
                    }
                }finally{
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }

}
