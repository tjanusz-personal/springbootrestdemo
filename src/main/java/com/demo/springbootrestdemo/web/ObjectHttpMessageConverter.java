package com.demo.springbootrestdemo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

/**
 * This is a custom HttpMessageConverter added due to the fact that Spring MVC isn't handling our
 *
 * @RequestBody correctly with our content-type of 'application/x-www-form-urlencoded'. This code
 * was directly taken from the snippet of stackoverflow comment. https://stackoverflow.com/questions/16449141/spring-mvc-and-the-requestbody-annotation-with-x-www-form-urlencoded-data
 * https://stackoverflow.com/questions/4339207/http-post-with-request-content-type-form-not-working-in-spring-mvc-3
 *
 * We need to investigate this further before going live with this approach.
 */
public class ObjectHttpMessageConverter implements HttpMessageConverter<Object> {

  private static final LinkedMultiValueMap<String, String> LINKED_MULTI_VALUE_MAP =
      new LinkedMultiValueMap<>();
  private static final Class<? extends MultiValueMap<String, ?>> LINKED_MULTI_VALUE_MAP_CLASS =
      (Class<? extends MultiValueMap<String, ?>>) LINKED_MULTI_VALUE_MAP.getClass();
  private final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public boolean canRead(Class clazz, MediaType mediaType) {
    return objectMapper.canSerialize(clazz) && formHttpMessageConverter
        .canRead(MultiValueMap.class, mediaType);
  }

  @Override
  public boolean canWrite(Class clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public java.util.List<MediaType> getSupportedMediaTypes() {
    return formHttpMessageConverter.getSupportedMediaTypes();
  }

  @Override
  public Object read(Class clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    Map<String, String> input = formHttpMessageConverter
        .read(LINKED_MULTI_VALUE_MAP_CLASS, inputMessage).toSingleValueMap();
    return objectMapper.convertValue(input, clazz);
  }

  @Override
  public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException("");
  }

}
