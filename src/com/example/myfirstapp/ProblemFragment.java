package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

public class ProblemFragment extends Fragment implements AdapterView.OnItemClickListener {
	
	private static final String PROMPT = "com.example.myfirstapp.prompt";
	
	private OnProblemCompletedListener mCallback;
	private String mRawPrompt;
	private ArrayList<String> mOptions = new ArrayList<String>();
	private String mCorrectOption;
	private boolean mCorrect = true;
	
	public interface OnProblemCompletedListener {
        public void onProblemCompleted(boolean correct);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnProblemCompletedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProblemCompletedListener");
        }
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		

        // Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.problem_view, container, false);
		
		mRawPrompt = getArguments().getString(PROMPT);
		mOptions = processPrompt(mRawPrompt);
		mCorrectOption = mOptions.get(0);
		Collections.shuffle(mOptions, new Random(System.nanoTime()));

		TextView textPrompt = (TextView)view.findViewById(R.id.prompt_textview);
        textPrompt.setText(mRawPrompt.replaceAll("\\{(.*)\\}", "_____"));
		
        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<String>(
     		   getActivity()
     		 , android.R.layout.simple_selectable_list_item
     		 , mOptions);
      
        ListView listView = (ListView) view.findViewById(R.id.options_listview);
        listView.setAdapter(optionsAdapter);
        listView.setOnItemClickListener(this);
		
        return view;
    }
	
	private ArrayList<String> processPrompt(String rawPrompt){
		//rawPrompt = rawPromptArg;
        Matcher promptMatcher = Pattern.compile("\\{(.*)\\}").matcher(rawPrompt);

        ArrayList<String> options = new ArrayList<String>();
        
        promptMatcher.find();
    	String[] ss = promptMatcher.group(1).split(",");
    	for(String s : ss)
    	{
    		options.add(s.trim());
    	}

        return options;
	}
	
	public void onItemClick(AdapterView<?> av, View v, int index, long id) {
		String selectedOption = mOptions.get(index);
		
		boolean correct = selectedOption == mCorrectOption;
		
		String regex = "\\{(.*)\\}";
        Matcher promptMatcher = Pattern.compile(regex).matcher(mRawPrompt);
        
        // Why does java's regex engine have this unnecessary state????
        promptMatcher.find();
		int start = promptMatcher.start();
		int end = start + selectedOption.length();

		SpannableString text = new SpannableString(promptMatcher.replaceAll(selectedOption));
    	
		if(correct){
			mCallback.onProblemCompleted(mCorrect);
		}else{
			mCorrect = false; // once you get it wrong, you can't get it right.
    		text.setSpan(new StrikethroughSpan(), start, end, 0);
    	}
    	TextView promptView = (TextView)getView().findViewById(R.id.prompt_textview);
    	promptView.setText(text);
	}

	public static ProblemFragment newInstance(String string) {
		ProblemFragment newFragment = new ProblemFragment();
		Bundle args = new Bundle();
		args.putString(PROMPT, string);
		newFragment.setArguments(args);
		return newFragment;
	}
}
