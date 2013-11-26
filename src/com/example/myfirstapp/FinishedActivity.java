package com.example.myfirstapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class FinishedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finished);
		
		double score = getIntent().getDoubleExtra(StartActivity.SCORE, 0.0);
		
		TextView scoreView = (TextView)findViewById(R.id.score_textview);
		
		String formattedScore = String.format("%d%%", Math.round(score*100.0));
		
		scoreView.setText(formattedScore);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finished, menu);
		return true;
	}

}
