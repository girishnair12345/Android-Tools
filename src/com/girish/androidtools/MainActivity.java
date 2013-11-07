package com.girish.androidtools;

import com.girish.android.tools.InfiniteToast;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	Button btnPrefActivity,btnInfiniteToast,btnTimeToast;
	EditText edtTime;
	private Context mContext;
	InfiniteToast inf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = getApplicationContext();
		
		
		btnPrefActivity = (Button) findViewById(R.id.btnPrefActivity);
		btnPrefActivity.setOnClickListener(this);
		
		btnInfiniteToast = (Button) findViewById(R.id.btnInfiniteToast);
		btnInfiniteToast.setOnClickListener(this);
		
		btnTimeToast = (Button) findViewById(R.id.btnTimeToast);
		btnTimeToast.setOnClickListener(this);
		edtTime = (EditText) findViewById(R.id.edtTime);
	}

	@Override
	public void onClick(View veiw) {
		switch (veiw.getId()) {
		case R.id.btnPrefActivity:
			startActivity(new Intent(MainActivity.this,MyPreferenceActivity.class));
			break;
			
		case R.id.btnInfiniteToast:
			
			if(btnInfiniteToast.getText().equals("Infinite Toast")){
				inf = new InfiniteToast(mContext, "Toast");
				inf.setText("Infinite Toast");
				inf.show();
				btnInfiniteToast.setText("Stop Toast");
			}else{
				inf.cancel();
				btnInfiniteToast.setText("Infinite Toast");
			}
			break;
			
		case R.id.btnTimeToast:
			try{
				int time = Integer.parseInt(edtTime.getText().toString());
				inf = new InfiniteToast(mContext, "Time for " + time + " seconds");
				inf.setDuration(time);
				inf.show();
			}catch (NumberFormatException e) {
				Toast.makeText(mContext, "Time is not valid", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		
		
	}
	}
