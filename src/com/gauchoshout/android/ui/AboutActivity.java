package com.gauchoshout.android.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.gauchoshout.android.R;

public class AboutActivity extends SherlockActivity implements OnClickListener{
	
	private Button coolbeans_btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		coolbeans_btn = (Button) findViewById(R.id.coolbeans_btn);
		coolbeans_btn.setOnClickListener(this);
	}
	//coolbeans is basically an OK button, except it says cool beans.
	// Cool beans? Cool beans.
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.coolbeans_btn:
			finish();
			break;
		}
	}

}