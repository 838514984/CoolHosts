package com.find.coolhosts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MoreFunctions extends Activity{
	
	Button catHosts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morefunctions);
		
		

		
		catHosts=(Button)findViewById(R.id.catHosts);
		catHosts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCatHosts=new Intent(MoreFunctions.this,CatHosts.class);
				MoreFunctions.this.startActivity(intentCatHosts);
			}
		});
	}
}
