package com.gauchoshout.android.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class RegisterResponse {
    
    public enum ErrorCode {
        INVALID_JSON,
        NO_ERROR,
        MISSING_INFORMATION,
        PASSWORD_MISMATCH,
        USERNAME_TAKEN,
        EMAIL_TAKEN;
    }
    
    private final ErrorCode errorCode;
    private final User user;
    private final String token;
    //error codes from server
    private static final Map<Integer, ErrorCode> ERROR_CODES;
    static {
        Map<Integer, ErrorCode> map = new HashMap<Integer, ErrorCode>();
        map.put(-1, ErrorCode.INVALID_JSON);
        map.put(0, ErrorCode.NO_ERROR);
        map.put(1, ErrorCode.MISSING_INFORMATION);
        map.put(2, ErrorCode.PASSWORD_MISMATCH);
        map.put(3, ErrorCode.USERNAME_TAKEN);
        map.put(4, ErrorCode.EMAIL_TAKEN);
        ERROR_CODES = Collections.unmodifiableMap(map);
    }
    
    public RegisterResponse(int errorCode, User user, String token) {
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
