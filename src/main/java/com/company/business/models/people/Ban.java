package com.company.business.models.people;

import java.time.LocalDate;

public class Ban {
  private Integer id;
  private int customerId;
  private LocalDate from;
  private LocalDate to;

  public Ban(Integer id, int customerId, LocalDate from, LocalDate to) {
    this.id = id;
    this.customerId = customerId;
    this.from = from;
    this.to = to;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public LocalDate getFrom() {
    return from;
  }

  public void setFrom(LocalDate from) {
    this.from = from;
  }

  public LocalDate getTo() {
    return to;
  }

  public void setTo(LocalDate to) {
    this.to = to;
  }
}
