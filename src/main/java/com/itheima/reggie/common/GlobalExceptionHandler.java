package com.itheima.reggie.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author yy
 * @version 1.0
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        if(ex.getMessage().contains("Duplicate entry")){
            String[] spilt=ex.getMessage().split(" ");
            String msg=spilt[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误！");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }

}
