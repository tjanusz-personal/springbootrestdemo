package com.demo.springbootrestdemo;

import com.demo.springbootrestdemo.web.ObjectHttpMessageConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootrestdemoApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void contextLoads() {
	}

	@Test
	public void ensureCustomObjectHttpMessageConverterLoaded() {
		String beanName = ObjectHttpMessageConverter.class.getSimpleName();
		ObjectHttpMessageConverter converter = (ObjectHttpMessageConverter) applicationContext
				.getBean(beanName);
		RequestMappingHandlerAdapter handler = (RequestMappingHandlerAdapter) applicationContext.getBean
				("requestMappingHandlerAdapter");
		assertThat(handler.getMessageConverters(), hasItem(converter));
	}



}
