package com.gauchoshout.android.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gauchoshout.android.R;
import com.gauchoshout.android.data.Comment;

// This class dynamically creates a list of comments upon viewing a post.
public class CommentListAdapter extends ArrayAdapter<Comment>{
	Context ctx;
	int layoutResourceId;
	Comment[] data;
	
	public CommentListAdapter(Context ctx, int layoutResourceId, Comment[] data){
		super(ctx, layoutResourceId, data);
		this.ctx = ctx;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		CommentHolder holder;
		
		if(row==null){
			LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new CommentHolder();
			holder.comment = (TextView) row.findViewById(R.id.comment_row_title);
		
			row.setTag(holder);
		}
		else{
			holder = (CommentHolder) row.getTag();
		}
		
		Comment commentContent = data[position];
		holder.comment.setText(commentContent.getText());
		
		return row;
		
		
	}
	
	static class CommentHolder {
		TextView comment;
	}
}
