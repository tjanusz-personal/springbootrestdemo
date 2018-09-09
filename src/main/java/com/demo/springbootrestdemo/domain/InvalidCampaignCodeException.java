package com.demo.springbootrestdemo.domain;

public class InvalidCampaignCodeException extends IllegalArgumentException {

  public InvalidCampaignCodeException(String s) {
    super("Invalid Campaign Code: " + s);
  }
}
