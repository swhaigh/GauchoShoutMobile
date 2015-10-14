package com.gauchoshout.android.ui;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.gauchoshout.android.R;
import com.gauchoshout.android.data.SubmitResponse;
import com.gauchoshout.android.http.WebServiceTask;
import com.gauchoshout.android.util.JsonParser;
import com.gauchoshout.android.util.SessionStore;

public class CreatePostActivity extends SherlockActivity implements OnClickListener {
    
    private EditText mTitleEt;
    private EditText mBodyEt;
    private CheckBox mPostAnonymous;
    private RadioButton mStory;
    private RadioButton mQuestion;
    private RadioButton mShout;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private boolean postDisabled = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_post);
		
		mTitleEt = (EditText) findViewById(R.id.title);
		mBodyEt = (EditText) findViewById(R.id.body);
		mStory = (RadioButton) findViewById(R.id.storyRadioButton);
		mStory.setOnClickListener(this);
		mQuestion = (RadioButton) findViewById(R.id.questionRadioButton);
		mQuestion.setOnClickListener(this);
		mShout = (RadioButton) findViewById(R.id.shoutRadioButton);
		mShout.setOnClickListener(this);
		mSubmitBtn = (Button) findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mCancelBtn = (Button) findViewById(R.id.cancel_btn);
		mCancelBtn.setOnClickListener(this);
		mPostAnonymous = (CheckBox) findViewById(R.id.anon);
	
	}
	
	private void submit(){
		String title = mTitleEt.getText().toString();
		String body = mBodyEt.getText().toString();
		boolean anon = mPostAnonymous.isChecked();
		String type = "0";
		// Types: 0 for a shout, 1 for a question, 2 for a story.
		// These are passed to the web server for handling
		
		if (mQuestion.isChecked()) type = "1"; else if (mStory.isChecked()) type = "2";
		NameValuePair titleParam  = new BasicNameValuePair("title", title);
		NameValuePair bodyParam = new BasicNameValuePair("content", body);
		NameValuePair anonParam = new BasicNameValuePair("anonymous", (anon) ? "1" : "0");
		NameValuePair userNameParam = new BasicNameValuePair("username", SessionStore.getUsername(CreatePostActivity.this));
		NameValuePair tokenParam = new BasicNameValuePair("token", SessionStore.getToken(CreatePostActivity.this));
		NameValuePair typeParam = new BasicNameValuePair("type", type);
		SubmitTask submitTask = new SubmitTask();
        submitTask.execute(titleParam, bodyParam, anonParam, userNameParam, tokenParam, typeParam);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.submit_btn:
        	if (validEntries() && !postDisabled) {
        		postDisabled = true;
        		mSubmitBtn.setEnabled(false);
        		mCancelBtn.setEnabled(false);
        		submit();
        	}
            break;
        case R.id.cancel_btn:
            finish();
        case R.id.storyRadioButton:
        	if(mQuestion.isChecked() == true || mShout.isChecked() == true){
        		mQuestion.setChecked(false);
        		mShout.setChecked(false);
        	}
        	break;
        case R.id.questionRadioButton:
        	if(mStory.isChecked() == true || mShout.isChecked() == true){
        		mStory.setChecked(false);
        		mShout.setChecked(false);
        	}
        	break;
        case R.id.shoutRadioButton:
        	if(mStory.isChecked() == true || mQuestion.isChecked() == true){
        		mStory.setChecked(false);
        		mQuestion.setChecked(false);
        	}
        	break;
        }
    }
    
    
    public boolean validEntries(){ // Input validation of the finest degree
    	int titleLength = mTitleEt.getText().toString().length();
    	int contentLength = mBodyEt.getText().toString().length();
    	if (titleLength > 100) {
    		Toast.makeText(CreatePostActivity.this, "Title must be less than 100 characters", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	else if (titleLength == 0){
    		Toast.makeText(CreatePostActivity.this, "You must have a title", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	else if (contentLength == 0){
    		Toast.makeText(CreatePostActivity.this, "Your post must have content", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	return true;
    }
    private void handleLogin(SubmitResponse submitResponse) {
        switch (submitResponse.getErrorCode()) {
        case INVALID_JSON:
            Toast.makeText(CreatePostActivity.this, "invalid json", Toast.LENGTH_SHORT).show();
            break;
        case NO_ERROR:
            Toast.makeText(CreatePostActivity.this, "post saved successfully", Toast.LENGTH_SHORT).show();
            finish();
            break;
        case MISSING_TOKEN:
            Toast.makeText(CreatePostActivity.this, "wrong credentials", Toast.LENGTH_SHORT).show();
            break;
        case MISSING_TITLE:
            Toast.makeText(CreatePostActivity.this, "user not found", Toast.LENGTH_SHORT).show();
            break;
        case MISSING_CONTENT:
            Toast.makeText(CreatePostActivity.this, "missing information", Toast.LENGTH_SHORT).show();
            break;
        case INVALID_TOKEN:
        	Toast.makeText(CreatePostActivity.this, "invalid token", Toast.LENGTH_SHORT).show();
        	break;
        case NO_ASSOC_USER:
        	Toast.makeText(CreatePostActivity.this, "no associated user with token", Toast.LENGTH_SHORT).show();
        	break;
        case TOKEN_MISMATCH:
        	Toast.makeText(CreatePostActivity.this, "token mismatch", Toast.LENGTH_SHORT).show();
        	break;
        case REPOST:
        	Toast.makeText(CreatePostActivity.this, "this content has already been submitted", Toast.LENGTH_SHORT).show();
        	break;
        }
    }
    private class SubmitTask extends WebServiceTask {
        
        private static final String POST_URL = "https://gauchoshout.herokuapp.com/post.php?action=post";
        
        private SubmitResponse response;

        public SubmitTask() {
            super(POST_URL, CreatePostActivity.this);
        }
        
        @Override
        protected Boolean doInBackground(NameValuePair... pairs) {
            boolean success = super.doInBackground(pairs);
            if (success) {
                response = JsonParser.parseSubmitResponse(raw_json);
            }
            
            return success;
        }
        
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                handleLogin(response);
            } else {
                Toast.makeText(CreatePostActivity.this, "epic flailure", Toast.LENGTH_SHORT).show();
                // oh no! \*o*/
            }
            postDisabled = false;
    		mSubmitBtn.setEnabled(true);
    		mCancelBtn.setEnabled(true);
        }
        
    }
}
