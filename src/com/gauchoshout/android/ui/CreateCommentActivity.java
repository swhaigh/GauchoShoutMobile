package com.gauchoshout.android.ui;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.gauchoshout.android.R;
import com.gauchoshout.android.data.CommentResponse;
import com.gauchoshout.android.http.WebServiceTask;
import com.gauchoshout.android.util.JsonParser;
import com.gauchoshout.android.util.SessionStore;

public class CreateCommentActivity extends SherlockActivity implements OnClickListener {
    

    private EditText mBody;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private boolean postDisabled = false;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_comment);
		
		mBody = (EditText) findViewById(R.id.body);

		mSubmitBtn = (Button) findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mCancelBtn = (Button) findViewById(R.id.cancel_btn);
		mCancelBtn.setOnClickListener(this);

	
	}
	
	private void submit(){

		String body = mBody.getText().toString();
		int parentId = getIntent().getExtras().getInt("parent");
		//parent is the unique id associated with the post this person is commenting on
		String parent = Integer.toString(parentId);
		NameValuePair bodyParam = new BasicNameValuePair("text", body);
		NameValuePair userNameParam = new BasicNameValuePair("username", SessionStore.getUsername(CreateCommentActivity.this));
		NameValuePair tokenParam = new BasicNameValuePair("token", SessionStore.getToken(CreateCommentActivity.this));
		NameValuePair parentParam = new BasicNameValuePair("parent", parent);
		//username and token pairs always sent to the server for authentication 
		SubmitTask submitTask = new SubmitTask();
        submitTask.execute(bodyParam, userNameParam, tokenParam, parentParam);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.submit_btn:
        	if (validEntries() && !postDisabled) {
        		// postDisabled, so when you press Submit you can't keep on pressing submit
        		postDisabled = true;
        		mSubmitBtn.setEnabled(false);
        		mCancelBtn.setEnabled(false);
        		submit();
        	}
            break;
        case R.id.cancel_btn:
            finish();
        }
    }
    
    
    public boolean validEntries(){
    	// basic input validation
    	int contentLength = mBody.getText().toString().length();
    	if (contentLength == 0){
    		Toast.makeText(CreateCommentActivity.this, "Your post must have content", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	return true;
    }
    // Error checking, my dear watson.
    // This is based on the enums established in the CommentResponse.java file
    private void handleLogin(CommentResponse submitResponse) {
        switch (submitResponse.getErrorCode()) {
        case INVALID_JSON:
            Toast.makeText(CreateCommentActivity.this, "invalid json", Toast.LENGTH_SHORT).show();
            break;
        case NO_ERROR:
            Toast.makeText(CreateCommentActivity.this, "post saved successfully", Toast.LENGTH_SHORT).show();
            finish();
            break;
        case PARENT_PARAMETER_IS_EMPTY:
            Toast.makeText(CreateCommentActivity.this, "post is empty", Toast.LENGTH_SHORT).show();
            break;
        case POST_NOT_FOUND:
            Toast.makeText(CreateCommentActivity.this, "post not found", Toast.LENGTH_SHORT).show();
            break;
        case TOKEN_DOESNT_EXIST:
            Toast.makeText(CreateCommentActivity.this, "token does not exist", Toast.LENGTH_SHORT).show();
            break;
        case TOKEN_POINTS_TO_NO_USER:
        	Toast.makeText(CreateCommentActivity.this, "token points to no user", Toast.LENGTH_SHORT).show();
        	break;
        case TOKEN_MISMATCH:
        	Toast.makeText(CreateCommentActivity.this, "token mismatch", Toast.LENGTH_SHORT).show();
        	break;
        }
    }
    private class SubmitTask extends WebServiceTask {
        
        private static final String POST_URL = "https://gauchoshout.herokuapp.com/postcomment.php";
        
        private CommentResponse response;

        public SubmitTask() {
            super(POST_URL, CreateCommentActivity.this);
        }
        
        @Override
        protected Boolean doInBackground(NameValuePair... pairs) {
        	// do in background so app doesn't freeze waiting for responses
            boolean success = super.doInBackground(pairs);
            if (success) {
                response = JsonParser.parseCommentResponse(raw_json);
            }
            
            return success;
        }
        
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                handleLogin(response);
            } else {
                Toast.makeText(CreateCommentActivity.this, "epic flailure", Toast.LENGTH_SHORT).show();
                //EPIC FLAILURE, something went wrong, throw your arms up and run away \*o*/
            }
            // Request handled, errors make buttons active again, success will close app.
            postDisabled = false;
    		mSubmitBtn.setEnabled(true);
    		mCancelBtn.setEnabled(true);
        }
        
    }
}
