package com.novelbio.springonly.factory;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringFactory extends ApplicationObjectSupport {

	private static ApplicationContext context;
//	@PostConstruct
//	public void init() throws ServletException {
//		context = getWebApplicationContext();
//	}
	
	/**
	 * 根据id拿到spring容器中的bean
	 * @param id
	 * @return
	 */
	public static Object getBean(String id) {
		if (context == null) {
			context = new ClassPathXmlApplicationContext("spring.xml");
		}
		return context.getBean(id);
	}
	public static void main(String[] args) {
		SpringFactory.getBean("springHelper");
	}
	
	/**
	 * 根据class拿到spring容器中的bean
	 * @param <T>
	 * @param id
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType) {
		if (context == null) {
			context = new ClassPathXmlApplicationContext("spring.xml");
		}
		return context.getBean(requiredType);
	}
	
	/**
	 * 取得全局变量
	 * @return
	 */
//	public static String getGlobeParam(String key){
//		GlobeParameters globeParameters = context.getBean(GlobeParameters.class);
//		return globeParameters.getMapParams().get(key);
//	}
//	
//	public static GlobeParameters getGlobeParameters() {
//		GlobeParameters globeParameters = context.getBean(GlobeParameters.class);
//		return globeParameters;
//	}
}
