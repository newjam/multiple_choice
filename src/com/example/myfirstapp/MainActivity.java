package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static CharSequence[] sentences = {
		 "Los gatos {est‡n, est‡, son} dormiendo sobre la cama."
		,"Mi gato {es, est‡, son} negro."
		,"Los caballos {corren, est‡n, nadan} r‡pidamente"};
	
	public ArrayList<String> options = new ArrayList<String>();
	private static int sentenceIndex = 0;
	private String correctOption;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        TextView footerView = (TextView)findViewById(R.id.footer);        
        footerView.setText(String.format("%d/%d", sentenceIndex + 1, sentences.length));
        
        CharSequence rawPrompt = sentences[sentenceIndex];//getText(R.string.question1);
        
        //CurrentProblem p = new CurrentProblem();
        
        String prompt = setPrompt(rawPrompt);

        TextView promptView = (TextView)findViewById(R.id.prompt);        
        promptView.setText(prompt);
        
        Collections.shuffle(options, new Random(System.nanoTime()));
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_selectable_list_item
        		, options);
        
        /*android.R.layout.simple_list_item_multiple_choice */ /*android.R.layout.simple_list_item_1*/
        
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int index, long id) {

				String response = options.get(index);
				
				boolean correct = response == correctOption;
				
				String regex = "\\{(.*)\\}";
		        Matcher promptMatcher = Pattern.compile(regex).matcher(sentences[sentenceIndex]);
		        
		        // Why does java's regex engine have this unnecessary state????
		        promptMatcher.find();
				int start = promptMatcher.start();
				int end = start + response.length();
				
		    	//CharSequence response = correct ? "correct" : "incorrect";
		    	
		    	//Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
		    	
				SpannableString text = new SpannableString(promptMatcher.replaceAll(response));
		    	
				//CharSequence text = promptMatcher.replaceAll(response);
				
		    	if(correct) {
		    		Intent intent;
		    		if(sentenceIndex >= sentences.length - 1)
		    		{
		    			//finished
		    			intent = new Intent(MainActivity.this, FinishedActivity.class);
		    		}else
		    		{
		    			sentenceIndex++;
			    		intent = new Intent(MainActivity.this, MainActivity.class);
		    		}
		    		startActivityForResult(intent, 500);
		            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		    	}else{
		    		text.setSpan(new StrikethroughSpan(), start, end, 0);
		    	}
		    	TextView promptView = (TextView)findViewById(R.id.prompt);
		    	promptView.setText(text);
		    	
		    	/*if(correct){
		    		CharSequence completedSentence = getString(R.string.prompt).replace("_____", correctOption);
		    		TextView prompt = (TextView)findViewById(R.id.prompt);
		    		prompt.setText(completedSentence);
		    	}*/
			}
		});
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private String setPrompt(CharSequence rawPromptArg) {
		String regex = "\\{(.*)\\}";
		//rawPrompt = rawPromptArg;
        Matcher promptMatcher = Pattern.compile(regex).matcher(sentences[sentenceIndex]);

        
        options = new ArrayList<String>();
        
        promptMatcher.find();
    	String[] ss = promptMatcher.group(1).split(",");
    	for(String s : ss)
    	{
    		options.add(s.trim());
    	}

        correctOption = options.get(0);
        
        String prompt = promptMatcher.replaceAll("_____");
        
		return prompt;
    }
    
    public void onClick(View v) {
    	
    	/*TextView tv = (TextView)v;
    	
    	boolean correct = tv.getId() == correctAnswer;
    	
    	CharSequence response = correct ? "correct" : "incorrect";
    	
    	Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    	
    	
    	if(correct){
    		CharSequence completedSentence = getString(R.string.prompt).replace("_____", tv.getText());
    		TextView prompt = (TextView)findViewById(R.id.prompt);
    		prompt.setText(completedSentence);
    	}*/
    	
//    	// Dynamically create a button.
//    	TextView mytv = new TextView(this);
//    	
//    	
//    	mytv.setText("Dynamic TextView");
//    	//LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//    	LinearLayout ll = (LinearLayout)findViewById(R.id.ll);
//    	ll.addView(mytv);

    	//TextView tv = (TextView) findViewById (R.id.textView1);
    	
    	//v.setText("blah");
    }
}
