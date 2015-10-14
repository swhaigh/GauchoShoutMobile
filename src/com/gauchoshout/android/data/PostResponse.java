package com.gauchoshout.android.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PostResponse{

    public enum ErrorCode {
        INVALID_JSON,
        NO_ERROR,
        NO_USER_TOKEN_GIVEN,
        TOKEN_GIVEN_DOESNT_EXIST, // should prompt to login
        NO_RESULTS_TO_RETURN,
        USERNAME_NOT_FOUND,
        NO_USERNAME_GIVEN, 
        USERNAME_TOKEN_INVALID, // should prompt to login
        NO_TOKEN_ASSOCIATED_WITH_USER; // should prompt to login
    }
    
	private final ErrorCode  errorCode;
	private final String     token;
	private final List<Post> posts;
	private final String     nextPage;
    private static final Map<Integer, ErrorCode> ERROR_CODES;
    //error code associations
    static {
        Map<Integer, ErrorCode> map = new HashMap<Integer, ErrorCode>();
        map.put(-1, ErrorCode.INVALID_JSON);
        map.put(0, ErrorCode.NO_ERROR);
        map.put(1, ErrorCode.NO_USER_TOKEN_GIVEN);
        map.put(2, ErrorCode.TOKEN_GIVEN_DOESNT_EXIST);
        map.put(3, ErrorCode.NO_RESULTS_TO_RETURN);
        map.put(4, ErrorCode.USERNAME_NOT_FOUND);
        map.put(5, ErrorCode.NO_USERNAME_GIVEN);
        map.put(6, ErrorCode.USERNAME_TOKEN_INVALID);
        map.put(7, ErrorCode.NO_TOKEN_ASSOCIATED_WITH_USER);
        ERROR_CODES = Collections.unmodifiableMap(map);
    }
	
	public PostResponse(int errorCode, String token, List<Post> post, String nextPage) {
		this.errorCode = ERROR_CODES.get(errorCode);
		this.token = token;
		this.posts = post;
		this.nextPage = nextPage;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getToken() {
		return token;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public String getNextPage() {
		return nextPage;
	} 
	
}