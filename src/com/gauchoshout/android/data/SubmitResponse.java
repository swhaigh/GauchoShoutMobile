package com.gauchoshout.android.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SubmitResponse {
    
    public enum ErrorCode {
        INVALID_JSON,
        NO_ERROR,
        MISSING_TOKEN,
        MISSING_TITLE,
        MISSING_CONTENT,
        INVALID_TOKEN,
        NO_ASSOC_USER,
        TOKEN_MISMATCH,
        REPOST;
    }
    
    private final ErrorCode errorCode;
    private final String token;
    
    private static final Map<Integer, ErrorCode> ERROR_CODES;
    static {
        Map<Integer, ErrorCode> map = new HashMap<Integer, ErrorCode>();
        map.put(-1, ErrorCode.INVALID_JSON);
        map.put(0, ErrorCode.NO_ERROR);
        map.put(1, ErrorCode.MISSING_TOKEN);
        map.put(2, ErrorCode.MISSING_TITLE);
        map.put(3, ErrorCode.MISSING_CONTENT);
        map.put(4, ErrorCode.INVALID_TOKEN);
        map.put(5, ErrorCode.NO_ASSOC_USER);
        map.put(6, ErrorCode.TOKEN_MISMATCH);
        map.put(7, ErrorCode.REPOST);
        ERROR_CODES = Collections.unmodifiableMap(map);
    }
    
    public SubmitResponse(int errorCode, String token) {
        this.errorCode = ERROR_CODES.get(errorCode);
        this.token = token;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getToken() {
        return token;
    }
    
}
