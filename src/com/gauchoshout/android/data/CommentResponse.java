package com.gauchoshout.android.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommentResponse {
	// Error codes returned by server
	public enum ErrorCode{
		INVALID_JSON,
		NO_ERROR,
		PARENT_PARAMETER_IS_EMPTY,
		POST_NOT_FOUND,
		TOKEN_DOESNT_EXIST,
		TOKEN_POINTS_TO_NO_USER,
		TOKEN_MISMATCH;
	}
	
	private final ErrorCode errorCode;
	private final String token;
	private final List<Comment> comments;
	private static final Map<Integer, ErrorCode> ERROR_CODES;

	// Int error codes associated with each error.
	// These get returned by the server in the json object
	// e.g. error: 0, token: <string> indicated successful post.
    static {
        Map<Integer, ErrorCode> map = new HashMap<Integer, ErrorCode>();
        map.put(-1, ErrorCode.INVALID_JSON);
        map.put(0, ErrorCode.NO_ERROR);
        map.put(1, ErrorCode.PARENT_PARAMETER_IS_EMPTY);
        map.put(2, ErrorCode.POST_NOT_FOUND);
        map.put(3, ErrorCode.TOKEN_DOESNT_EXIST);
        map.put(4, ErrorCode.TOKEN_POINTS_TO_NO_USER);
        map.put(5, ErrorCode.TOKEN_MISMATCH);
        ERROR_CODES = Collections.unmodifiableMap(map);
    }
    
    public CommentResponse(int errorCode, String token, List<Comment> comment){
    	this.errorCode = ERROR_CODES.get(errorCode);
    	this.token = token;
    	this.comments = comment;
    }
	
    public ErrorCode getErrorCode(){
    	return errorCode;
    }
    
    public String getToken(){
    	return token;
    }
	
    public List<Comment> getComments(){
    	return comments;
    }
}
