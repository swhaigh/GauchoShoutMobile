package com.gauchoshout.android.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gauchoshout.android.data.Comment;
import com.gauchoshout.android.data.CommentResponse;
import com.gauchoshout.android.data.LoginResponse;
import com.gauchoshout.android.data.Post;
import com.gauchoshout.android.data.PostResponse;
import com.gauchoshout.android.data.RegisterResponse;
import com.gauchoshout.android.data.SubmitResponse;
import com.gauchoshout.android.data.User;

/**
 * Utility class to handle all json parsing. Each public method takes
 * a string of raw json and returns a java object encapsulating that
 * information.
 * 
 */
public final class JsonParser {

    private JsonParser() {}
    
    public static RegisterResponse parseNewAccount(String raw_json) {
    	int errorCode;
    	User user;
    	String token;
    	
    	try {
    		JSONObject json = new JSONObject(raw_json);
            errorCode = json.getInt("error");
            
            if (json.isNull("user")) {
                user = null;
            } else {
                JSONObject userJson = json.getJSONObject("user");
                user = parseUserJson(userJson);
            }
            
            token = json.getString("token");
    		
    	} catch (JSONException e) {
    		errorCode = -1;
    		user = null;
    		token = null;
    	}
    	return new RegisterResponse(errorCode, user, token);
    	
    }
    
    public static SubmitResponse parseSubmitResponse(String raw_json) {
    	int errorCode;
    	String token;
    	try {
    		JSONObject json = new JSONObject(raw_json);
    		errorCode = json.getInt("error");
    		token = json.getString("token");
    	} catch (JSONException e) {
    		errorCode = -1;
    		token = null;
    	}
    	return new SubmitResponse(errorCode, token);
    }
    
    public static LoginResponse parseLoginResponse(String raw_json) {
        int errorCode;
        User user;
        String token;
        
        try {
            JSONObject json = new JSONObject(raw_json);
            errorCode = json.getInt("error");
            
            if (json.isNull("user")) {
                user = null;
            } else {
                JSONObject userJson = json.getJSONObject("user");
                user = parseUserJson(userJson);
            }
            
            token = json.getString("token");
        } catch (JSONException e) {
            errorCode = -1;
            user = null;
            token = null;
        }
        
        return new LoginResponse(errorCode, user, token);
    }
    
    public static PostResponse parsePostResponse(String raw_json) {
        int errorCode;
        String token;
        List<Post> posts = new ArrayList<Post>();
        String nextPage;
        
        try {
            JSONObject json = new JSONObject(raw_json);
            errorCode = json.getInt("error");
            
            if (json.isNull("posts")) {
                posts = null;
            } else {
                JSONArray jsonArray = json.getJSONArray("posts");
                
                for(int i = 0; i < jsonArray.length(); i++) {
                	JSONObject postJson = jsonArray.getJSONObject(i);
                	Post post = parsePostJson(postJson);
                	posts.add(post);
                }
            }
            
            if (json.isNull("nextPage")) {
            	nextPage = null;
            } else {
            	nextPage = json.getString("nextPage");
            }
            
            token = json.getString("token");
        } catch (JSONException e) {
            errorCode = -1;
            posts = null;
            token = null;
            nextPage = null;
        }
        
        return new PostResponse(errorCode, token, posts, nextPage);
        
    }
    
    public static CommentResponse parseCommentResponse(String raw_json) {
    	int errorCode;
    	String token;
    	List<Comment> comments = new ArrayList<Comment>();
    	
    	try {
    		JSONObject json = new JSONObject(raw_json);
    		errorCode = json.getInt("error");
    		
    		if (json.isNull("comments")) {
    			comments = null;
    			Log.d("JsonParser", "comments is null");
    		} else {
    			JSONArray jsonArray = json.getJSONArray("comments");
    			
    			for (int i=0; i < jsonArray.length(); i++) {
    				JSONObject commentJson = jsonArray.getJSONObject(i);
    				Comment comment = parseCommentJson(commentJson);
    				comments.add(comment);
    			}
    		}
    		
    		token = json.getString("token");
    		
    	} catch (JSONException e) {
    		Log.d("JsonParser", e.getMessage());
    		errorCode = -1;
    		token = null;
    		comments = null;
    	}
    	
    	return new CommentResponse(errorCode, token, comments);
    }
    
    private static Comment parseCommentJson(JSONObject commentJson) throws JSONException {
    	int id = commentJson.getInt("id");
    	int parentId = commentJson.getInt("parentid");
    	int points = commentJson.getInt("points");
    	String text = commentJson.getString("text");
    	String timestamp = commentJson.getString("timestamp");
    	String username = commentJson.getString("username");
    	return new Comment(username, text, timestamp, parentId, id, points);
	}

	private static Post parsePostJson(JSONObject post) throws JSONException{
    	int id = post.getInt("id");
    	String title = post.getString("title");
    	String content = post.getString("content");
    	String timeStamp = post.getString("timestamp");
    	User op = parseUserJson(post.getJSONObject("op"));
    	int positive = post.getInt("positive");
    	int negative = post.getInt("negative");
    	int voted = post.getInt("voted");
    	return new Post(id, title, content, op, positive, negative, voted, timeStamp);
    	
    }
    
    private static User parseUserJson(JSONObject user) throws JSONException {
        String id = user.getString("id");
        String username = user.getString("username");
        String email = user.getString("email");
        String access = user.getString("access");
        String details = user.getString("details");
        String gender = user.getString("gender");
        return new User(id, username, email, access, details, gender);
    }
    
}
