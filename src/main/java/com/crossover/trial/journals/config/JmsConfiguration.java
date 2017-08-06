package com.crossover.trial.journals.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

/**
* The JmsConfiguration class includes the configurations related to JMS and ActiveMQ, 
* like the in-memory ActiveMQ and the JmsTemplate.
 */
@Configuration
@EnableJms
public class JmsConfiguration {

	/** Maximum retry count to be able to send mail notification. */
	private static final int RETRY_COUNT = 3;
	
	@Bean
	public ConnectionFactory jmsConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"vm://localhost?broker.persistent=false&jms.redeliveryPolicy.maximumRedeliveries=" + RETRY_COUNT);

		connectionFactory.setObjectMessageSerializationDefered(true);
		connectionFactory.setCopyMessageOnSend(false);

		return new CachingConnectionFactory(connectionFactory);
	}
	
	@Bean
	  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(jmsConnectionFactory());
	    factory.setConcurrency("3-10");
	    return factory;
	  }	
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(jmsConnectionFactory());
		jmsTemplate.setDefaultDestination(new ActiveMQQueue("queue.mailbox"));
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.setReceiveTimeout(2000);
		return jmsTemplate;
	}

}
