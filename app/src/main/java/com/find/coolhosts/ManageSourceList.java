package com.find.coolhosts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by find on 16-7-5.
 */
public class ManageSourceList extends Activity{
    private ListView source_list_view;
    private ManageSourceListAdapter mAdapter;
    private ArrayList<SourceInfo> source_list_data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_sources);
        source_list_data = new ArrayList<SourceInfo>();
        source_list_view = (ListView)findViewById(R.id.source_list_view);
        source_list_data.add(new SourceInfo(0, "http://www.findspace.name/hosts"));
        source_list_data.add(new SourceInfo(1, "http://www.findspace.name/hosts2"));
        mAdapter = new ManageSourceListAdapter(source_list_data, this);
        source_list_view.setAdapter(mAdapter);
    }
}
