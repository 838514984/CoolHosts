package com.find.coolhosts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by find on 16-7-4.
 */
public class Manage_source_list_adapter extends BaseAdapter{
    // 要填充的数据
    public ArrayList<Source_Info> source_list;
    // checkbox的选中状态
    public HashMap<Integer, Boolean> checkboxStatus;
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    // TODO： 还需要从这里传入原来已经选好的状态

    public Manage_source_list_adapter(ArrayList<Source_Info> a_list, Context context){
        this.context = context;
        this.source_list = a_list;
        this.inflater = LayoutInflater.from(context);
        checkboxStatus = new HashMap<Integer, Boolean>();
        init_Data();
    }
    private void init_Data(){
        for(int i = 0; i < source_list.size(); i++){
            checkboxStatus.put(i, false);
        }
    }
    @Override
    public int getCount() {
        return source_list.size();
    }

    @Override
    public Object getItem(int position) {
        return source_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder  = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list, null);
            holder.source_url = (EditText)convertView.findViewById(R.id.source_url);
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.source_url.setText(source_list.get(position).url);
        holder.cb.setChecked(checkboxStatus.get(position));
        return  convertView;
    }
    public static class ViewHolder{
        EditText source_url;
        CheckBox cb;
    }
}
