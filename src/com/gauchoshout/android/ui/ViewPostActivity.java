package com.gauchoshout.android.ui;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gauchoshout.android.R;
import com.gauchoshout.android.data.Comment;
import com.gauchoshout.android.data.CommentResponse;
import com.gauchoshout.android.data.SubmitResponse;
import com.gauchoshout.android.http.WebServiceTask;
import com.gauchoshout.android.util.JsonParser;
import com.gauchoshout.android.util.SessionStore;

public class ViewPostActivity extends SherlockFragmentActivity implements OnClickListener{
	
	private Button chillButton;
	private Button notchillButton;	
	private TextView usernameText;
	private TextView userDescText;
	private TextView titleText;
	private TextView postBodyText;
	private ImageView avatar;
	private ListView commentsList;
	private int postId;
	private int voted;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_post);
		
		chillButton = (Button)findViewById(R.id.chill_button);
		chillButton.setOnClickListener(this);
		notchillButton = (Button)findViewById(R.id.not_chill_button);
		notchillButton.setOnClickListener(this);
		usernameText = (TextView)findViewById(R.id.username);
		userDescText = (TextView)findViewById(R.id.user_desc);
		titleText = (TextView)findViewById(R.id.post_title);
		postBodyText = (TextView)findViewById(R.id.post_body);
		avatar = (ImageView)findViewById(R.id.user_pic);
		commentsList = (ListView) findViewById(R.id.comments_list);
		usernameText.setText("");
		userDescText.setText("");
		titleText.setText("");
		postBodyText.setText("");
		populate();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        return true;
    }
	// populate the text fields with important information.
	public void populate(){
		usernameText.setText("");
		String title = getIntent().getExtras().getString("title");
		postId = getIntent().getExtras().getInt("id");
		String content = getIntent().getExtras().getString("content");
		//String timestamp = getIntent().getExtras().getString("timestamp");
		String op = getIntent().getExtras().getString("op");
		String opDetails = getIntent().getExtras().getString("opDetails");
		String opGender = getIntent().getExtras().getString("gender");
		int access = getIntent().getExtras().getInt("access");
		// So the web server responds with either a string username for OP or a string null. 
		// it's a string null because something is lost in translation between the php and java
		if (op.equals("null")){
			op = "anonymous";
			opDetails = "Anonymous user.";
		}
		// Set their avatar based on gender, anonymity or dev level.
		if (opGender.equals("f")){
			avatar.setImageResource(R.drawable.female);
		} else if (opGender.equals("m")){
			avatar.setImageResource(R.drawable.male);
		} else {
			avatar.setImageResource(R.drawable.anon);
		} 
		if (access >= 9) {
			avatar.setImageResource(R.drawable.gsdev);
		}
		
		titleText.setText(title);
		postBodyText.setText(content);
		usernameText.setText(op);
		userDescText.setText(opDetails);
		getComments();
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent i;
        switch (item.getItemId()) {
        case R.id.post_menu_add:
        	i = new Intent(this, CreateCommentActivity.class);
        	Bundle extra = new Bundle();
        	extra.putInt("parent", postId); 
        	// Bundle post IDs so the next activity can have that info for retrieving/posting comments.
        	i.putExtras(extra);
        	startActivity(i);
        	return true;
        case android.R.id.home:
			finish();
        case R.id.post_menu_refresh:
        	getComments();
			return true;
		}
        
        return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {				
		switch (v.getId()) {
		case R.id.chill_button:
			upChill();
			break;
		case R.id.not_chill_button:
			downChill();
			break;
		}
	}
	// Send a chill rating to the server. Server stores who votes on what so duplicate votes are ignored.
	public void upChill(){
		//String url = "https://gauchoshout.com/vote.php";
		String username = SessionStore.getUsername(this);
		String token = SessionStore.getToken(this);
		NameValuePair usernamePair = new BasicNameValuePair("username", username);
		NameValuePair tokenPair = new BasicNameValuePair("token", token);
		NameValuePair postPair = new BasicNameValuePair("post", Integer.toString(postId));
		NameValuePair votePair = new BasicNameValuePair("vote", Integer.toString(1));
		Toast.makeText(ViewPostActivity.this, "Chill Rating +1!", Toast.LENGTH_SHORT).show();
		new Vote(this).execute(usernamePair, tokenPair, postPair, votePair);
		
		//send a request to server with post data: username, token, post(id) and vote (1 for up)
		
	}
	/* if a  user has already voted oppositely on something, their vote is subtracted from positive/negative,
	* then their vote is taken and put into their new vote.
	* example: user already voted on something "chill", that post now is rated positive: 2, negative: 0.
	* User decides they hate the post and they want to smite them with the power of democracy.
	* user changes vote to "not chill"
	* server checks old ratings and corrects the positive and negative to 1, 1 respectively.
	* all is well in the world
	*/
	// Send a not chill rating to the server. Server stores who votes on what so duplicate votes are ignored.
	public void downChill(){
		//String url = "https://gauchoshout.com/vote.php";
		String username = SessionStore.getUsername(this);
		String token = SessionStore.getToken(this);
		NameValuePair usernamePair = new BasicNameValuePair("username", username);
		NameValuePair tokenPair = new BasicNameValuePair("token", token);
		NameValuePair postPair = new BasicNameValuePair("post", Integer.toString(postId));
		NameValuePair votePair = new BasicNameValuePair("vote", Integer.toString(0));
		new Vote(this).execute(usernamePair, tokenPair, postPair, votePair);
		Toast.makeText(ViewPostActivity.this, "Chill Rating: -1!", Toast.LENGTH_SHORT).show();
		
		//send a request to server with post data: username, token, post(id) and vote (0 for down)
		
	}
	public void getComments(){
		String username = SessionStore.getUsername(this);
		String token = SessionStore.getToken(this);
		NameValuePair usernamePair = new BasicNameValuePair("username", username);
		NameValuePair tokenPair = new BasicNameValuePair("token", token);
		NameValuePair parentPair = new BasicNameValuePair("parent", Integer.toString(postId));
		new GetCommentsTask(this).execute(usernamePair, tokenPair, parentPair);
	}
	
	private class Vote extends WebServiceTask {
		
		SubmitResponse response = null;
		
		private static final String URL = "https://gauchoshout.herokuapp.com/vote.php";
		
		public Vote(Context ctx){
			super(URL, ViewPostActivity.this);
		}
		
		@Override
		protected Boolean doInBackground(NameValuePair... params) {
			boolean success = super.doInBackground(params);
			if (success) {
				response = JsonParser.parseSubmitResponse(raw_json);
			}
			
			return success;
		}
		
		@Override
		protected void onPostExecute(Boolean success) {
			if (success) {		
				
				if (tokenInvalid) {
					Toast.makeText(ViewPostActivity.this, "Session has expired. Please login again.", Toast.LENGTH_SHORT).show();
					SessionStore.clear(ViewPostActivity.this);
					Intent i = new Intent(ViewPostActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				} 
			}
			else {
				DialogHelper.showErrorDialog(ViewPostActivity.this, "Network error.");
			}
		}
		
		
		
	}
	
	private class GetCommentsTask extends WebServiceTask {
		
		CommentResponse commentResponse = null;
		
		private static final String URL = "https://gauchoshout.herokuapp.com/comments.php";

		public GetCommentsTask(Context ctx) {
			super(URL, ViewPostActivity.this);
		}
		
		@Override
		protected Boolean doInBackground(NameValuePair... params) {
			boolean success = super.doInBackground(params);
			if (success) {
				commentResponse = JsonParser.parseCommentResponse(raw_json);
			}
			
			return success;
		}
		
		@Override
		protected void onPostExecute(Boolean success) {
			if (success) {
				List<Comment> comments = commentResponse.getComments();
				
				if (tokenInvalid) {
					Toast.makeText(ViewPostActivity.this, "Session has expired. Please login again.", Toast.LENGTH_SHORT).show();
					SessionStore.clear(ViewPostActivity.this);
					Intent i = new Intent(ViewPostActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				} else {
					CommentListAdapter adapter = new CommentListAdapter(ViewPostActivity.this, R.layout.comment_row, comments.toArray(new Comment[0]));
					commentsList.setAdapter(adapter);
				}
			} else {
				DialogHelper.showErrorDialog(ViewPostActivity.this, "Network error.");
			}
		}
		
	}

}
