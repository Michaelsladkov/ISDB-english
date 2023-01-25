package com.company.business.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
  CommonController.class, WorkersController.class, CustomersController.class, AdminController.class
})
public class ControllersHandler {
  private static final Logger logger = LoggerFactory.getLogger(ControllersHandler.class);

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String exceptionHandler(Exception e) {
    logger.error("Not caught exception", e);
    return "errorPage";
  }
}
