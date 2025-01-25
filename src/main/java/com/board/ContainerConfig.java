package com.board;


import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//내장 톰캣 환경 설정
@Configuration
public class ContainerConfig {

	@Value("${tomcat.ajp.protocol}")
	String ajpProtocol;
	
	@Value("${tomcat.ajp.port}")
	int ajpPort;
	
	@Value("${tomcat.ajp.enabled}")
	boolean ajpEnabled;
	
	@Bean
	public TomcatServletWebServerFactory servlet() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		
		if(ajpEnabled) {			
			Connector ajpConnector = new Connector(ajpProtocol);
			ajpConnector.setPort(ajpPort);
			ajpConnector.setSecure(false);
			ajpConnector.setAllowTrace(false);
			ajpConnector.setScheme("http");
			tomcat.addAdditionalTomcatConnectors(ajpConnector);
			((AbstractAjpProtocol<?>)ajpConnector.getProtocolHandler()).setSecretRequired(false);			
		}
		
		return tomcat;
	}
}
