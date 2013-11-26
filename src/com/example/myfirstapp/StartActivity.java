package com.example.myfirstapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
//import android.app.FragmentActivity;

import android.widget.TextView;
//import android.widget.Toast;

public class StartActivity extends FragmentActivity implements ProblemFragment.OnProblemCompletedListener {
	
	public  final static String SCORE = "com.example.myfirstapp.percentCorrect";
	
	private int mProblemIndex = 0;
	private int mNumWrong = 0;
	
	private static String[] mSentences = {
		 "Los gatos {est‡n, est‡, son} dormiendo sobre la cama."
		,"Mi gato {es, est‡, son} negro."
		,"Los caballos {corren, est‡n, nadan} r‡pidamente"};
	
	void updateFragment()
	{
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ProblemFragment newFragment = ProblemFragment.newInstance(mSentences[mProblemIndex]);		
		ft.add(R.id.problem_container, (Fragment)newFragment);
        ft.commit();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        updateFragment();
        updateFooter();
    }

    private void updateFooter()
    {
    	TextView footerView = (TextView)findViewById(R.id.footer);        
        footerView.setText(String.format("%d/%d", mProblemIndex + 1, mSentences.length));
    }
   
	@Override
	public void onProblemCompleted(boolean correct) {
		mProblemIndex++;
		
		mNumWrong += correct ? 0 : 1;
		
		if(mProblemIndex >= mSentences.length)
		{
			// done with the problem set.
			// transition to the score screen.
			double score = ((double)mSentences.length - (double)mNumWrong)/(double)mSentences.length;
			Intent intent = new Intent(this, FinishedActivity.class);
			intent.putExtra(SCORE, score);
			startActivity(intent);
		}else{
			updateFooter();
	        
			// transition to next problem
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
			Fragment newFragment = ProblemFragment.newInstance(mSentences[mProblemIndex]);
			ft.replace(R.id.problem_container, newFragment);
			ft.commit();
		}
	}
}