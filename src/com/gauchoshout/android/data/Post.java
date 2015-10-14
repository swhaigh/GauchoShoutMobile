package com.gauchoshout.android.data;

//Class that has attributes for important user post content

public class Post {
    
    private final int id;
    private final String title;
    private final String content;
    private final String timeStamp;
    private final int    positive;
    private final int    negative;
    private final int    voted;
    private final User op;
    
	public Post(int id, String title, String content, User op,
			int positive, int negative, int voted, String timeStamp) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.op = op;
		this.positive = positive;
		this.negative = negative;
		this.voted = voted;
		this.timeStamp = timeStamp;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public User getOp() {
		return op;
	}

	public int getPositive() {
		return positive;
	}

	public int getNegative() {
		return negative;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public int getVoted() {
		return voted;
	}
	
    
}
