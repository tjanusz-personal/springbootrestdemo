package com.demo.springbootrestdemo.domain.request;

import com.demo.springbootrestdemo.domain.validation.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveUrlRequest {

  public SaveUrlRequest() {}

  @NotNull
  private String url;

  private String referrerUrl = "";

  @NotNull
  @UUID
  private String accountCode;

  @NotNull
  @UUID
  private String campaignKey;

  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  public String getReferrerUrl() {
    return referrerUrl;
  }

  @JsonProperty("ref")
  public void setReferrerUrl(String referrerUrl) {
    this.referrerUrl = referrerUrl;
  }

  public String getAccountCode() {
    return accountCode;
  }

  @JsonProperty("acc_code")
  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getCampaignKey() {
    return campaignKey;
  }

  @JsonProperty("camp_key")
  public void setCampaignKey(String campaignKey) {
    this.campaignKey = campaignKey;
  }

}
