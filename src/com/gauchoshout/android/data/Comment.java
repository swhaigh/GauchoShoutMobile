package com.gauchoshout.android.data;

//Comment class with important attributes

public class Comment {
	
	private final String username;
	private final String text;
	private final String timeStamp;
	private final int id;
	private final int post_id;
	private final int points;
	
	public Comment(String username, String text, String timeStamp, int id, int post_id, int points){
		this.username = username;
		this.text = text;
		this.timeStamp = timeStamp;
		this.id = id;
		this.post_id = post_id;
		this.points = points;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getText(){
		return text;
	}
	
	public String getTimeStamp(){
		return timeStamp;
	}
	
	public int getId(){
		return id;
	}
	
	public int getPostId(){
		return post_id;
	}
	
	public int getPoints(){
		return points;
	}
}
