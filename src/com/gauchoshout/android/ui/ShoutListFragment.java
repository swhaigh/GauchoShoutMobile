package com.gauchoshout.android.ui;


public class ShoutListFragment extends PostListFragment {

	private static final String SHOUTS_URL = "https://gauchoshout.herokuapp.com/get.php?action=shouts";
	
	@Override
	public String getUrl() {
		return SHOUTS_URL;
	}

}