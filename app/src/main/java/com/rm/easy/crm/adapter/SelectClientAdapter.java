package com.rm.easy.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.rm.easy.crm.R;
import com.rm.easy.crm.jsonBean.Data;

import java.util.List;

/**
 * Created by Easy.D on 2016/11/13.
 */
public class SelectClientAdapter extends ArrayAdapter<Data> {

    private int resourceId;

    public SelectClientAdapter(Context context, int textViewResourceId, List<Data> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView clientName = (TextView)view.findViewById(R.id.select_client_listview_item_clientname);
        TextView clientPhone = (TextView)view.findViewById(R.id.select_client_listview_item_clientphone);
        TextView clientAdd = (TextView)view.findViewById(R.id.select_client_listview_item_clientadd);
        clientName.setText(data.getClientName());
        clientPhone.setText(data.getClientPhone());
        clientAdd.setText(data.getClientAdd());
        return view;
    }
}
