package com.semtleWebGroup.youtubeclone.domain.member.dto;

public enum SignCommonResponse {
    
    SUCCESS(0, "Success"), FAIL(-1, "Fail");
    
    int code;
    String msg;
    
    SignCommonResponse(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
    
    public int getCode(){
        return code;
    }
    
    public String getMsg(){
        return msg;
    }
}