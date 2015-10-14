package com.gauchoshout.android.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.gauchoshout.android.data.VoteResponse.ErrorCode;

public class VoteResponse {
	public enum ErrorCode{
		INVALID_JSON,
		NO_ERROR,
		POST_PARAMETER_IS_EMPTY,
		POST_NOT_FOUND,
		TOKEN_DOESNT_EXIST,
		TOKEN_MISMATCH;
	}
	
	private final ErrorCode errorCode;
	private final String token;
	
	private static final Map<Integer, ErrorCode> ERROR_CODES;

    static {
        Map<Integer, ErrorCode> map = new HashMap<Integer, ErrorCode>();
        map.put(-1, ErrorCode.INVALID_JSON);
        map.put(0, ErrorCode.NO_ERROR);
        map.put(1, ErrorCode.POST_PARAMETER_IS_EMPTY);
        map.put(2, ErrorCode.POST_NOT_FOUND);
        map.put(3, ErrorCode.TOKEN_DOESNT_EXIST);
        map.put(4, ErrorCode.TOKEN_MISMATCH);
        ERROR_CODES = Collections.unmodifiableMap(map);
    }
    
    public VoteResponse(int errorCode, String token){
    	this.errorCode = ERROR_CODES.get(errorCode);
    	this.token = token;
    }
	
    public ErrorCode getErrorCode(){
    	return errorCode;
    }
    
    public String getToken(){
    	return token;
    }

}
