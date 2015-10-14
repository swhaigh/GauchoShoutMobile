package com.gauchoshout.android.ui;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.gauchoshout.android.R;
import com.gauchoshout.android.data.Post;
import com.gauchoshout.android.data.PostResponse;
import com.gauchoshout.android.http.WebServiceTask;
import com.gauchoshout.android.ui.StoryListAdapter.StoryHolder;
import com.gauchoshout.android.util.JsonParser;
import com.gauchoshout.android.util.SessionStore;

//Class to load stored posts on server

public abstract class PostListFragment extends SherlockListFragment {
	
	private String nextPage = null;
	private View footer;
	private Button footerBtn;
	
	public abstract String getUrl();
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		
		LayoutInflater inflater = getLayoutInflater(state);
		footer = inflater.inflate(R.layout.footer, null);
		footerBtn = (Button) footer.findViewById(R.id.footer_btn);
		footerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadPosts();
			}
		});
		
		loadPosts();
	}
	
	public void refresh() {
		loadPosts();
	}

	private void loadPosts() {
		String username = SessionStore.getUsername(getActivity());
		String token = SessionStore.getToken(getActivity());
		
		NameValuePair usernamePair = new BasicNameValuePair("username", username);
		NameValuePair tokenPair = new BasicNameValuePair("token", token);
		
		GetPostsTask task;
		if (nextPage == null) {
			task = new GetPostsTask(getUrl(), getActivity());
		} else {
			task = new GetPostsTask(nextPage, getActivity());
		}
		
		task.execute(usernamePair, tokenPair);

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		StoryHolder story = (StoryListAdapter.StoryHolder) v.getTag();
		Post post = story.post;
		Intent i = new Intent(getActivity(), ViewPostActivity.class);
		Bundle extra = new Bundle();
		extra.putString("title", post.getTitle());
	    extra.putInt("id", post.getId());
	    extra.putString("content", post.getContent());
	    extra.putString("timestamp", post.getTimeStamp());
	    extra.putString("op", post.getOp().getUsername());
	    extra.putString("opDetails", post.getOp().getDetails());
	    extra.putString("gender", post.getOp().getGender());
	    String accessString = post.getOp().getAccess();
	    int access = Integer.parseInt(accessString);
	    extra.putInt("access", access);
		i.putExtras(extra);
		getActivity().startActivity(i);
	}
	
	private class GetPostsTask extends WebServiceTask {
		
		public GetPostsTask(String uri, Context ctx) {
			super(uri, getActivity());
		}

		PostResponse postResponse = null;

		@Override
		protected Boolean doInBackground(NameValuePair... params) {
			boolean success = super.doInBackground(params);
			if (success) {
				postResponse = JsonParser.parsePostResponse(raw_json);
			}
			
			return success;
		}
		
		@Override
		protected void onPostExecute(Boolean success) {
			if (success) {
				List<Post> posts = postResponse.getPosts();
				
				if (tokenInvalid) {
					Toast.makeText(getActivity(), "Session has expired. Please login again.", Toast.LENGTH_SHORT).show();
					SessionStore.clear(getActivity());
					Intent i = new Intent(getActivity(), LoginActivity.class);
					startActivity(i);
					getActivity().finish();
				} else {
					if (postResponse.getNextPage() != null) {
						nextPage = postResponse.getNextPage();
						getListView().addFooterView(footer, null, false);
					}
					
					StoryListAdapter adapter = new StoryListAdapter(getActivity(), R.layout.story_row, posts.toArray(new Post[0]));
					setListAdapter(adapter);
					
					if (postResponse.getNextPage() == null) {
						nextPage = null;
						getListView().removeFooterView(footer);
					}
				}
			} else {
				DialogHelper.showErrorDialog(getSherlockActivity(), "Network error.");
			}
		}	
		
	}

}
