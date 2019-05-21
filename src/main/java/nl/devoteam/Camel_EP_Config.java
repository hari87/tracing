package nl.devoteam;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.context.annotation.Bean;

import javax.jms.ConnectionFactory;

public class Camel_EP_Config {

    @Bean
    public ActiveMQConnectionFactory coreConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("http://localhost:61616");
        // the below setting is to be checked with peers & mohammed.!!!
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean
    public JmsConfiguration jmsConfiguration(ActiveMQConnectionFactory coreConnectionFactory) {
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        ConnectionFactory connectionFactory = new PooledConnectionFactory(coreConnectionFactory);
        jmsConfiguration.setConnectionFactory(connectionFactory);
        return jmsConfiguration;
    }

    @Bean(name = "activemq")
    public ActiveMQComponent activeMQComponent(JmsConfiguration jmsConfiguration) {
        ActiveMQComponent component = new ActiveMQComponent();
        component.setConfiguration(jmsConfiguration);
        return component;
    }
}
