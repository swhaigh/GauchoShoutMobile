package com.gauchoshout.android.ui;


public class StoryListFragment extends PostListFragment {
	
	private static final String STORIES_URL = "https://gauchoshout.herokuapp.com/get.php?action=stories";
	
	@Override
	public String getUrl() {
		return STORIES_URL;
	}
    
}
