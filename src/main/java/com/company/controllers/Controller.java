package com.company.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {

  @GetMapping(path = "sir")
  public String sir() {
    return "SIR, YES SIR!";
  }
}
