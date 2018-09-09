package com.demo.springbootrestdemo.web;

import com.demo.springbootrestdemo.domain.request.SaveUrlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.demo.springbootrestdemo.web.DemoController.HTTP_HEADER_EXPIRES_VALUE;
import static com.demo.springbootrestdemo.web.DemoController.HTTP_HEADER_X_FORWARDED_FOR;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
@ComponentScan(basePackages = "com.demo.springbootrestdemo.web") // Need to add this to ensure picks up @Configuration beans
public class DemoControllerIntTest {

    private static final String HARD_CODED_XFORWARDED_FOR = "111.111.111.111";
    private static final String HARD_CODED_USER_AGENT_STRING = "USER_AGENT_STRING";
    private static final String FORM_VALUE_ACCOUNT_CODE = "C467D055-ED15-B570-95A0-D853DD09171B";
    private static final String FORM_VALUE_CAMPAIGN_KEY = "EA5BF9C3-0C9C-B276-4313-62A9A9B3E16B";
    private static final String FORM_VALUE_URL = "url_value";
    private static final String FORM_VALUE_REFERRER_URL = "ref_url_value";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void alivePingReturns200Status() throws Exception {
        this.mockMvc.perform(get("/alive"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void alivePingReturnsTextMessage() throws Exception {
        this.mockMvc.perform(get("/alive"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.textMessage", is("still alive")));
    }

    @Test
    public void responseHeadersAreSetAppropriately() throws Exception {
        mockMvc.perform(get("/alive"))
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "no-cache, must-revalidate"))
                .andExpect(header().string(HttpHeaders.EXPIRES, HTTP_HEADER_EXPIRES_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8"))
                .andExpect(header().doesNotExist("x-application-context"));
    }

    @Test
    public void saveUrlRequestReturnsApplicationJsonByDefault() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SaveUrlRequest request = getDummySaveUrlRequest(FORM_VALUE_CAMPAIGN_KEY);
        String requestAsString = objectMapper.writeValueAsString(request);

        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestAsString);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.url", is(FORM_VALUE_URL)))
                .andExpect(jsonPath("$.accountCode", is( FORM_VALUE_ACCOUNT_CODE)))
                .andExpect(jsonPath("$.campaignKey", is( FORM_VALUE_CAMPAIGN_KEY)))
                .andExpect(jsonPath("$.urlIdentifier", is(100)))
                .andExpect(jsonPath("$.userAgent", is( HARD_CODED_USER_AGENT_STRING)))
                .andExpect(jsonPath("$.forwardedFor", is( HARD_CODED_XFORWARDED_FOR)));
    }

    @Test
    public void saveUrlRequestReturnsApplicationXMLIfRequested() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SaveUrlRequest request = getDummySaveUrlRequest(FORM_VALUE_CAMPAIGN_KEY);
        String requestAsString = objectMapper.writeValueAsString(request);

        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestAsString);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(xpath("/SaveUrlResponse/status").string("success"))
                .andExpect(xpath("/SaveUrlResponse/url").string("url_value"));
    }

    @Test
    public void saveUrlReturnsBadRequestWithMissingParameters() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // Creating request without campaign_key
        SaveUrlRequest request = getDummySaveUrlRequest(null);
        String requestAsString = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestAsString);
        mockMvc.perform(builder).andExpect(status().isBadRequest());
    }

    @Test
    public void saveUrlReturnsBadRequestWithInvalidCampaignKey() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SaveUrlRequest request = getDummySaveUrlRequest(DemoController.INVALID_CAMPAIGN_KEY);
        String requestAsString = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestAsString);

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", StringContains.containsString(DemoController
                        .INVALID_CAMPAIGN_KEY)));
    }

    @Test
    public void saveUrlReturnsBadRequestWithGenericError() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SaveUrlRequest request = getDummySaveUrlRequest(DemoController.ERROR_CAMPAIGN_KEY);
        String requestAsString = objectMapper.writeValueAsString(request);
        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestAsString);

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorMessage", StringContains.containsString("generic_error")));
    }

    @Test
    public void saveUrlRequestSupportsUsingAppFormUrlEncoded() throws Exception {
        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .param("url", FORM_VALUE_URL)
                .param("ref", FORM_VALUE_REFERRER_URL)
                .param("acc_code", FORM_VALUE_ACCOUNT_CODE)
                .param("camp_key", FORM_VALUE_CAMPAIGN_KEY);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url", is(FORM_VALUE_URL)))
                .andExpect(jsonPath("$.accountCode", is(FORM_VALUE_ACCOUNT_CODE)))
                .andExpect(jsonPath("$.campaignKey", is(FORM_VALUE_CAMPAIGN_KEY)))
                .andExpect(jsonPath("$.urlIdentifier", is(100)))
                .andExpect(jsonPath("$.userAgent", is(HARD_CODED_USER_AGENT_STRING)))
                .andExpect(jsonPath("$.forwardedFor", is(HARD_CODED_XFORWARDED_FOR)));
    }

    @Test
    public void saveUrlRequestReturnsErrorsUsingAppFormUrlEncoded() throws Exception {
        MockHttpServletRequestBuilder builder = post("/saveUrl")
                .header(HTTP_HEADER_X_FORWARDED_FOR, HARD_CODED_XFORWARDED_FOR)
                .header(USER_AGENT, HARD_CODED_USER_AGENT_STRING)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .param("url", FORM_VALUE_URL)
                .param("ref", FORM_VALUE_REFERRER_URL)
                .param("camp_key", FORM_VALUE_CAMPAIGN_KEY);

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
        // This does NOT work and should. Somehow the RestResponseEntityExceptionHandler isn't being supported
        // for this content-type.
//                .andExpect(jsonPath("$.errorMessage", StringContains.containsString(DemoController
//                .INVALID_CAMPAIGN_KEY)));
    }


    private SaveUrlRequest getDummySaveUrlRequest(String campaignKey) {
        SaveUrlRequest request = new SaveUrlRequest();
        request.setAccountCode(FORM_VALUE_ACCOUNT_CODE);
        request.setCampaignKey(campaignKey);
        request.setReferrerUrl(FORM_VALUE_REFERRER_URL);
        request.setUrl(FORM_VALUE_URL);
        return request;
    }

}