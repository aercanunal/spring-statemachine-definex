package com.definex.springstatemachinedefinex.umlconfig;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;

@Log4j2
@Configuration
@EnableStateMachineFactory
public class UmlStateMachineConfig extends StateMachineConfigurerAdapter<String,String> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(getListener());
    }

    private StateMachineListener<String,String> getListener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateEntered(State<String, String> state) {
                log.info("State entered {}", state.getId());
            }
        };
    }

    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new UmlStateMachineModelFactory("classpath:statemachine/order-statemachine/order-statemachine.uml");
    }

    @Override
    public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
        model.withModel().factory(modelFactory());
    }

    @Bean
    public Action<String, String> preOrderAction() {
        return context -> log.info("Executing preOrderAction for event {}",context.getEvent());
    }

    @Bean
    public Action<String, String> doPaymentAction() {
        return context -> log.info("Executing doPaymentAction for event {}",context.getEvent());
    }

    @Bean
    public Action<String, String> minLimitCountAction() {
        return context -> log.info("Executing minLimitCountAction for event {}",context.getEvent());
    }

    @Bean
    public Action<String, String> completeOrderAction() {
        return context -> log.info("Executing completeOrderAction for event {}",context.getEvent());
    }

    @Bean
    public Action<String, String> sendNotificationAction() {
        return context -> log.info("Executing sendNotificationAction for event {}",context.getEvent());
    }

    @Bean
    public Guard<String, String> validationOrderGuard() {
        return context -> {
            log.info("Executing isLowerThenMinCountGuard for event {}",context.getEvent());

            var result = Boolean.TRUE.equals(context.getExtendedState().getVariables().getOrDefault("validation",false));

            log.info("Validation result: {}",result);
            return result;
        };
    }

    @Bean
    public Guard<String, String> isLowerThenMinCountGuard() {
        return context -> {
            String event = context.getEvent();
            Long count = (Long)context.getMessageHeaders().getOrDefault("count", 0L);

            log.info("Executing isLowerThenMinCountGuard for event: {}", event);
            log.info("Count: {}", count);

            return count < 10;
        };
    }


}
