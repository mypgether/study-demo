package com.gether.bigdata;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@ControllerAdvice
class GlobalExceptionHandler {

  public static final String DEFAULT_ERROR_VIEW = "error";

  @ExceptionHandler(value = Throwable.class)
  public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response,
      Exception ex) throws Exception {
    ModelAndView mv = new ModelAndView();
    mv.addObject("exception", ex);
    mv.addObject("url", request.getRequestURL());
    mv.setViewName(DEFAULT_ERROR_VIEW);

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache, must-revalidate");
    try {
      response.getWriter().write(JSON.toJSONString(ex.getMessage()));
    } catch (IOException e) {
    }
    return mv;
  }

  @ExceptionHandler(value = RuntimeException.class)
  @ResponseBody
  public Object jsonErrorHandler(HttpServletRequest req, RuntimeException e) throws Exception {
    Map<String, String> map = Maps.newHashMap();
    map.put("url", req.getRequestURL().toString());
    map.put("errormsg", e.getMessage());
    return map;
  }
}