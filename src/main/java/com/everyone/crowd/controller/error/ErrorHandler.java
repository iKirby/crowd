package com.everyone.crowd.controller.error;

import com.everyone.crowd.entity.exception.ErrorCodeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ModelAndView handleError(ErrorCodeException e) {
        System.out.println("handle error");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code", e.getCode());
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setViewName("error-custom");
        return modelAndView;
    }
}
