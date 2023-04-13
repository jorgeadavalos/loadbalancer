package com.assoc.jad.loadbalancer;

import java.io.IOException;
import java.util.EnumSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import com.assoc.jad.loadbalancer.lbinstance.LoadBalancerFilter;
import com.assoc.jad.loadbalancer.tools.LoadBalancerStatic;

@EnableAutoConfiguration
@ComponentScan("com.assoc.jad.loadbalancer")
@SpringBootApplication
public class BootloadbalancerApplication {
	private static SpringApplication application = null;
	
	public static void main(String[] args) {
		application = new SpringApplication(BootloadbalancerApplication.class );
		application.run(args);
		
//		try {
//			resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("/webapp/*");
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}
	@Bean
	public ServletRegistrationBean<FacesServlet> servletRegistrationBean() {
				
		FacesServlet servlet = new FacesServlet();
		return new ServletRegistrationBean<FacesServlet>(servlet,"*.xhtml");
	}
	@Bean
	public ServletRegistrationBean<BalancerServlet> balancerServletRegistrationBean() {
		BalancerServlet servlet = new BalancerServlet();
		return new ServletRegistrationBean<BalancerServlet>(servlet,"/*");
	}
	@Bean
    public FilterRegistrationBean<LoadBalancerFilter> loadBalancerFilterRegistration() {

        FilterRegistrationBean<LoadBalancerFilter> register = new FilterRegistrationBean<LoadBalancerFilter>(new LoadBalancerFilter());
        register.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,DispatcherType.ASYNC, DispatcherType.ERROR));
        register.addUrlPatterns("/*");
        register.addInitParameter("script", System.getenv("LBHANDSHAKE"));
        return register;
    }
	@Bean
    public FilterRegistrationBean<RewriteFilter> rewriteFilter() {

        FilterRegistrationBean<RewriteFilter> register = new FilterRegistrationBean<RewriteFilter>(new RewriteFilter());
        register.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,DispatcherType.ASYNC, DispatcherType.ERROR));
        register.addUrlPatterns("/*");
        return register;
    }
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Bean
	public PropertiesFactoryBean appProperties(ResourceLoader resourceLoader) {
//		ConfigureXMLTemplates configureXMLTemplates = new ConfigureXMLTemplates("xml-templates");
//		configureXMLTemplates.run();

		PropertiesFactoryBean  properties = new PropertiesFactoryBean();
		try {
			Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(contextPath+"/*");
			for (Resource resource : resources) {
				LoadBalancerStatic.htmlFiles.put(resource.getFilename(),resource.getInputStream());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
}
