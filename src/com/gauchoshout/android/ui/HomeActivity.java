package com.gauchoshout.android.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gauchoshout.android.R;
import com.gauchoshout.android.util.SessionStore;


public class HomeActivity extends SherlockFragmentActivity {
    

    FragmentManager mFragMgr;
    
    private static final String STORIES_TAG = "stories";
    private static final String QUESTIONS_TAG = "questions";
    private static final String SHOUTS_TAG = "shouts";

    @Override
    public void onCreate(Bundle state) {
        
    	super.onCreate(state);
        
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setIcon(R.drawable.gs);
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Tab storiesTab = actionBar.newTab().setText("Stories")
                .setTabListener(new TabListener<StoryListFragment>(this, STORIES_TAG, StoryListFragment.class));
        actionBar.addTab(storiesTab);
        
        Tab questionsTab = actionBar.newTab().setText("Questions")
                .setTabListener(new TabListener<QuestionListFragment>(this, QUESTIONS_TAG, QuestionListFragment.class));
        actionBar.addTab(questionsTab);
        
        Tab shoutsTab = actionBar.newTab().setText("Shouts")
                .setTabListener(new TabListener<ShoutListFragment>(this, SHOUTS_TAG, ShoutListFragment.class));
        actionBar.addTab(shoutsTab);
        
        mFragMgr = getSupportFragmentManager();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
        case R.id.menu_add:
            i = new Intent(this, CreatePostActivity.class);
            startActivity(i);
            return true;
        case R.id.menu_refresh:
        	PostListFragment frag = (PostListFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        	frag.refresh();
        	return true;
        case R.id.menu_logout:
            SessionStore.clear(this);
            i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        case R.id.menu_about:
            i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private class TabListener<T extends Fragment> implements com.actionbarsherlock.app.ActionBar.TabListener {

        private Fragment mFrag;
        private final FragmentActivity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        
        public TabListener(FragmentActivity activity, String tag, Class<T> cls) {
            mActivity = activity;
            mTag = tag;
            mClass = cls;
        }
        
        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (mFrag == null) {
                mFrag = Fragment.instantiate(mActivity, mClass.getName());
                ft.replace(android.R.id.content, mFrag, mTag);
            } else {
                ft.attach(mFrag);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFrag != null) {
                ft.detach(mFrag);
            }
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // do nothing
        }
        
    }
    
}
    

