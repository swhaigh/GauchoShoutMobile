package com.gauchoshout.android.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gauchoshout.android.R;
import com.gauchoshout.android.data.Post;
import com.gauchoshout.android.data.User;

public class StoryListAdapter extends ArrayAdapter<Post> {
	
	Context ctx;
	int layoutResourceId;
	Post[] data;

	public StoryListAdapter(Context ctx, int layoutResourceId, Post[] data) {
		super(ctx, layoutResourceId, data);
		this.ctx = ctx;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StoryHolder holder;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new StoryHolder();
			holder.title = (TextView) row.findViewById(R.id.story_row_title);
			
			row.setTag(holder);
		} else {
			holder = (StoryHolder) row.getTag();
		}
		
		Post post = data[position];
		holder.title.setText(post.getTitle());
		holder.post = post;

		return row;
	}
	
	static class StoryHolder {
		TextView title;
		Post post;
		User op;
	}
	
}
