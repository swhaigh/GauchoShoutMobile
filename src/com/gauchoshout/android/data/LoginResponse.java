package com.gauchoshout.android.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class LoginResponse {
    
    public enum ErrorCode {
        INVALID_JSON,
        NO_ERROR,
        WRONG_CREDENTIALS,
        USER_NOT_FOUND,
        MISSING_INFORMATION;
    }
    
    private final ErrorCode errorCode;
    private final User user;
    private final String token;
    
	// Int error codes associated with each error.
	// These get returned by the server in the json object
    private static final Map<Integer, ErrorCode> ERROR_CODES;
    static {
        Map<Integer, ErrorCode> map = new HashMap<Integer, ErrorCode>();
        map.put(-1, ErrorCode.INVALID_JSON);
        map.put(0, ErrorCode.NO_ERROR);
        map.put(1, ErrorCode.WRONG_CREDENTIALS);
        map.put(2, ErrorCode.USER_NOT_FOUND);
        map.put(3, ErrorCode.MISSING_INFORMATION);
        ERROR_CODES = Collections.unmodifiableMap(map);
    }
    
    public LoginResponse(int errorCode, User user, String token) {
        this.errorCode = ERROR_CODES.get(errorCode);
        this.user = user;
        this.token = token;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
    
}
