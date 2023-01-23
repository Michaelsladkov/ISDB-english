package com.company.business.models;

import com.company.business.models.people.Customer;

import java.time.LocalDateTime;

public class Order {
  private Integer id;
  private Customer customer;
  private LocalDateTime time;
  private boolean closed;

  public Order(Integer id, Customer customer, LocalDateTime time, boolean closed) {
    this.id = id;
    this.customer = customer;
    this.time = time;
    this.closed = closed;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }
}
