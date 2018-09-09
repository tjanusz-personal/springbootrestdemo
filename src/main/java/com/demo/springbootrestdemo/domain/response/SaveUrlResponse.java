package com.demo.springbootrestdemo.domain.response;

public class SaveUrlResponse {

    private String status;
    private String url;
    private String accountCode;
    private String campaignKey;
    private long urlIdentifier;
    private String userAgent;
    private String forwardedFor;

    public SaveUrlResponse(String status, String url, String accountCode, String campaignKey, long urlIdentifier,
                           String userAgent, String forwardedFor) {
        this.status = status;
        this.url = url;
        this.accountCode = accountCode;
        this.campaignKey = campaignKey;
        this.urlIdentifier = urlIdentifier;
        this.userAgent = userAgent;
        this.forwardedFor = forwardedFor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getCampaignKey() {
        return campaignKey;
    }

    public void setCampaignKey(String campaignKey) {
        this.campaignKey = campaignKey;
    }

    public long getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(long urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getForwardedFor() {
        return forwardedFor;
    }

    public void setForwardedFor(String forwardedFor) {
        this.forwardedFor = forwardedFor;
    }
}
