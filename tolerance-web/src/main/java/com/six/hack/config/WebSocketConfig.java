package com.six.hack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.six.hack.QueueWorker;
import com.six.hack.ToleranceChecker;
import com.six.hack.WebController;
import com.six.hack.model.PresenceEventListener;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Bean
    public PresenceEventListener presenceEventListener(WebController controller) {
        PresenceEventListener presence = new PresenceEventListener(controller);
        return presence;
    }

    @Bean
    public ToleranceChecker toleranceChecker() {
        return new ToleranceChecker();
    }

    @Bean
    QueueWorker queueWorker() {
        return new QueueWorker();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/hello").withSockJS();
    }
}
