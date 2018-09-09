package com.demo.springbootrestdemo.web;

import com.demo.springbootrestdemo.domain.InvalidCampaignCodeException;
import com.demo.springbootrestdemo.domain.request.SaveUrlRequest;
import com.demo.springbootrestdemo.domain.response.DemoPingResponse;
import com.demo.springbootrestdemo.domain.response.SaveUrlResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.MediaType.*;

@RestController
public class DemoController {

    static final String HTTP_HEADER_EXPIRES_VALUE = "Sat, 26 Jul 1997 05:00:00 GMT";
    static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    public static final String INVALID_CAMPAIGN_KEY = "AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA";
    public static final String ERROR_CAMPAIGN_KEY = "BBBBBBBB-BBBB-BBBB-BBBB-BBBBBBBBBBBB";

    /**
     * This will force all HTTP response headers to always have these fields in them. In some of our microservices we
     * always expect certain HTTP response header values.
     * @param response
     */
    @ModelAttribute
    public void setStandardResponseHeaders(HttpServletResponse response) {
        // this shows standard header values to send back
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().mustRevalidate().getHeaderValue());
        response.setHeader(HttpHeaders.EXPIRES, HTTP_HEADER_EXPIRES_VALUE);
    }

    /**
     * Simple /alive method to ensure simple GET requests are working
     * @return
     */
    @GetMapping("/alive")
    public DemoPingResponse alive() {
        return new DemoPingResponse("still alive");
    }

    /**
     * SaveURL Post method. This combines a bunch of different stuff into a single method.
     * - consumes content type of either APP_JSON or FORM_URLENCODED_VALUE
     * - produces either XML or JSON responses
     * - Validates the request using Jackson
     * - Automatically pulls values out of the HTTP header
     * - Throws some exceptions at times which are mapped to HTTP status codes.
     * @param request
     * @param xForwardedFor
     * @param userAgent
     * @return SaveUrlResponse - response object with various request params added to validate things worked like
     * supposed to.
     */
    @PostMapping(value = "/saveUrl",
            consumes = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_FORM_URLENCODED_VALUE},
            produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_XML_VALUE}
    )
    public SaveUrlResponse saveUrl(@RequestBody @Valid SaveUrlRequest request,
                                   @RequestHeader(value = HTTP_HEADER_X_FORWARDED_FOR, defaultValue = "") String
                                           xForwardedFor,
                                   @RequestHeader(value = USER_AGENT, defaultValue = "") String userAgent
    ) {
        // Example of some domain type validation exception being thrown too
        if (INVALID_CAMPAIGN_KEY.equalsIgnoreCase(request.getCampaignKey())) {
            throw new InvalidCampaignCodeException(request.getCampaignKey());
        }

        // Example of some generic exception being thrown
        if (ERROR_CAMPAIGN_KEY.equalsIgnoreCase(request.getCampaignKey())) {
            throw new RuntimeException("generic_error");
        }

        // do something in domain/services here for now just create response object to serialize back
        SaveUrlResponse response = new SaveUrlResponse("success", request.getUrl(), request.getAccountCode(),
                request.getCampaignKey(), 100l, userAgent, xForwardedFor);
        return response;
    }


    /**
     * Controller level exception handler. This will convert our application exception to a HTTP status code
     * but we will be missing the body info which isn't ideal.
     */
//    @ExceptionHandler({InvalidCampaignCodeException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public void InvalidRequestParams() {
//    }

}
