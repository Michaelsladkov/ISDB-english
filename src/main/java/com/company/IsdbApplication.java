package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.URLClassLoader;

@SpringBootApplication
public class IsdbApplication {

  public static void main(String[] args) {
    SpringApplication.run(IsdbApplication.class, args);
  }

}
