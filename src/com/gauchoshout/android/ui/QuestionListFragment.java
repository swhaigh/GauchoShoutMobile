package com.gauchoshout.android.ui;


public class QuestionListFragment extends PostListFragment {

	private static final String QUESTIONS_URL = "https://gauchoshout.herokuapp.com/get.php?action=questions";
	
	@Override
	public String getUrl() {
		return QUESTIONS_URL;
	}
	
}
