package com.find.coolhosts;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by find on 16-7-5.
 * 管理自定义的源
 */
public class ManageSourceList extends Activity{
    private ListView source_list_view;
    private ManageSourceListAdapter mAdapter;
    private ArrayList<SourceInfo> source_list_data;
    private String source_list_file_name = "source_list";
    private SharedPreferences source_list_sp ;
    private int sum_nums;
    private String urls[];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_sources);
        source_list_sp = getSharedPreferences(source_list_file_name,MODE_PRIVATE);
        //获得自定义list中item的总数
        sum_nums = source_list_sp.getInt("sum", 0);
        urls = new String[sum_nums];
        for(int i =0;i<sum_nums;i++){
            urls[i] = source_list_sp.getString(""+i, "");
        }



    }
    /**在中途切出coolhosts以后的恢复，只需要把数据从内存中恢复出来，且加载到界面上，不需要重新从文件读*/
    @Override
    public void onResume(){
        super.onResume();
        source_list_data = new ArrayList<SourceInfo>();
        source_list_view = (ListView)findViewById(R.id.source_list_view);
        for (int i = 0; i < sum_nums; i++){
            source_list_data.add(new SourceInfo(i , urls[i]));
        }
        mAdapter = new ManageSourceListAdapter(source_list_data, this);
        source_list_view.setAdapter(mAdapter);

    }
}
