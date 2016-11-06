package com.rm.easy.crm.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Easy.D on 2016/11/6.
 */
public class GsonUtil {


    //Json数据解析成相对应的映射对象
    public static<T> T parseJsonWithGson(String jsonData, Class<T> type){
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData,type);
        return result;
    }

    //Json数据解析成相应的映射对象列表
    public static<T> ArrayList<T> parseJsonArrayWithGson(String jsonData, Class<T> type){
        Gson gson = new Gson();
        ArrayList<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>(){}.getType());
        return result;
    }

}
