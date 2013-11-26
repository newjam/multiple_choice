package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrentProblem {
	public ArrayList<String> options = new ArrayList<String>();
	private Matcher promptMatcher;
	private String correctOption;
	
	public String setPrompt(CharSequence rawPrompt) {
		String regex = "\\{(.*)\\}";
        Matcher promptMatcher = Pattern.compile(regex).matcher(rawPrompt);
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

	public boolean correct(int index) {
		return options.get(index) == correctOption;
	}
	
	public String correctSentence(int index) {
		return promptMatcher.replaceAll(options.get(index));
	}
	
	/*public String incorrectSentence(int index) {
		
	}*/
}
