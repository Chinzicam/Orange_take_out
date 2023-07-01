package com.czc.common;

/**
 * 分类删除业务异常处理
 * @author czc
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg){
        super(msg);
    }
}
