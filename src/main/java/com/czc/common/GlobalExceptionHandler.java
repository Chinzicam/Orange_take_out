package com.czc.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

import static com.sun.jmx.snmp.ThreadContext.contains;

/**
 * 全局异常处理
 * @author czc
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     *重复字段
     * @param ex
     * @return
     */
    //专门处理SQLIntegrityConstraintViolationException异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        //错误日志：Duplicate entry 'czc' for key 'employee.idx_username'
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return Result.error(msg);
        }
            return Result.error("未知错误");
    }

    /**
     * 分类删除业务异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
