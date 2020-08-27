package com.atguigu.gulimall.product.exceprion;

import com.atguigu.gulimall.common.exception.BizCodeEnume;
import com.atguigu.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * description: GulimallProductExceptionHandler
 * date: 2020-08-14 08:15
 * author: buelev
 * version: 1.0
 */
@Slf4j
//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product")
public class GulimallProductExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        Map<String, String> map = new HashMap<>();
        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                map.put(error.getField(), error.getDefaultMessage());
            });
        }
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R exceptionHandler(Throwable t) {
        log.error("程序异常:{}", t.getMessage());
        return R.error(BizCodeEnume.UNKNOWN_EXCEPTION.getCode(), BizCodeEnume.UNKNOWN_EXCEPTION.getMsg());
    }
}
