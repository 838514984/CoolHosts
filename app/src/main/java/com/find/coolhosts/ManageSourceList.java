package com.find.coolhosts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    private ArrayList<String> urls;
    private Button add_new_source;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_sources);
        source_list_sp = getSharedPreferences(source_list_file_name,MODE_PRIVATE);
        //获得自定义list中item的总数
        sum_nums = source_list_sp.getInt("sum", 0);
        urls = new ArrayList<String>();
        for(int i =0;i<sum_nums;i++){
            urls.add(i, source_list_sp.getString(""+i, ""));
        }
        add_new_source = (Button)findViewById(R.id.add_new_source);
        add_new_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(ManageSourceList.this);
                new AlertDialog.Builder(ManageSourceList.this).setTitle("请输入源地址").setIcon(
                        android.R.drawable.ic_dialog_info).setView(
                        et).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        urls.add(sum_nums, et.getText().toString());
                        source_list_data.add(new SourceInfo(sum_nums, urls.get(sum_nums)));
                        sum_nums++;
                        mAdapter.notifyDataSetChanged();
//                        this.appendOnConsole(getConsole(), true, R.string.customhostsaddressnote);
                        Toast.makeText(ManageSourceList.this, "Host源已经切换，仅此次有效，重启应用后恢复为默认的findspace的源", Toast.LENGTH_SHORT).show();
                    }})
                        .setNegativeButton("取消", null).show();
            }
        });



    }
    /**在中途切出coolhosts以后的恢复，只需要把数据从内存中恢复出来，且加载到界面上，不需要重新从文件读*/
    @Override
    public void onResume(){
        super.onResume();
        source_list_data = new ArrayList<SourceInfo>();
        source_list_view = (ListView)findViewById(R.id.source_list_view);
        for (int i = 0; i < sum_nums; i++){
            source_list_data.add(new SourceInfo(i , urls.get(i)));
        }
        mAdapter = new ManageSourceListAdapter(source_list_data, this);
        source_list_view.setAdapter(mAdapter);

    }
}
