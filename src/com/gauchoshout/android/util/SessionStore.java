package com.gauchoshout.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gauchoshout.android.data.LoginResponse;
import com.gauchoshout.android.data.RegisterResponse;
import com.gauchoshout.android.data.User;

public class SessionStore {
    
	private static final String KEY      = "session_store";
    private static final String TOKEN    = "token";
    private static final String ID       = "id";
    private static final String USERNAME = "username";
    private static final String EMAIL    = "email";
    private static final String ACCESS   = "access";
    private static final String DETAILS  = "details";
    private static final String GENDER   = "gender";

	public static boolean save(LoginResponse response, Context ctx) {
		Editor editor = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		User user = response.getUser();
		editor.putString(TOKEN, response.getToken());
		editor.putString(ID, user.getId());
		editor.putString(USERNAME, user.getUsername());
		editor.putString(EMAIL, user.getEmail());
		editor.putString(ACCESS, user.getAccess());
		editor.putString(DETAILS, user.getDetails());
		editor.putString(GENDER, user.getGender());
		return editor.commit();
	}
	public static boolean saveRegister(RegisterResponse response, Context ctx){
		Editor editor = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		User user = response.getUser();
		editor.putString(TOKEN, response.getToken());
		editor.putString(ID, user.getId());
		editor.putString(USERNAME, user.getUsername());
		editor.putString(EMAIL, user.getEmail());
		editor.putString(ACCESS, user.getAccess());
		editor.putString(DETAILS, user.getDetails());
		editor.putString(GENDER, user.getGender());
		return editor.commit();
	}
	
	public static boolean isValidSession(Context ctx) {
	    SharedPreferences prefs = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE);
	    String token = prefs.getString(TOKEN, null);
	    return (token != null);
	}
	
	public static String getToken(Context ctx) {
	    SharedPreferences prefs = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE);
	    return prefs.getString(TOKEN, null);
	}
	
	public static String getUsername(Context ctx) {
	    SharedPreferences prefs = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE);
	    return prefs.getString(USERNAME, null);
	}
	
	public static User getUser(Context ctx) {
	    SharedPreferences prefs = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		String id = prefs.getString(ID, null);
		String username = prefs.getString(USERNAME, null);
		String email = prefs.getString(EMAIL, null);
		String access = prefs.getString(ACCESS, null);
		String details = prefs.getString(DETAILS, null);
		String gender = prefs.getString(GENDER, null);
		return new User(id, username, email, access, details, gender);
	}
	
	public static void clear(Context ctx) {
	    Editor editor = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
	    editor.clear();
	    editor.commit();
	}

}
