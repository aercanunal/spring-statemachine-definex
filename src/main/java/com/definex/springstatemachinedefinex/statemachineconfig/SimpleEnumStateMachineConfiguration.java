package com.definex.springstatemachinedefinex.statemachineconfig;

import com.definex.springstatemachinedefinex.applicationreview.ApplicationReviewEvents;
import com.definex.springstatemachinedefinex.applicationreview.ApplicationReviewStates;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

@Configuration
@EnableStateMachine
@Log4j2
public class SimpleEnumStateMachineConfiguration extends StateMachineConfigurerAdapter<ApplicationReviewStates, ApplicationReviewEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<ApplicationReviewStates, ApplicationReviewEvents> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new StateMachineListenerAdapter<>() {
                    @Override
                    public void transition(Transition<ApplicationReviewStates, ApplicationReviewEvents> transition) {
                        ApplicationReviewEvents eventName = transition.getTrigger().getEvent();

                        ApplicationReviewStates source = transition.getSource().getId();
                        ApplicationReviewStates target = transition.getTarget().getId();

                        log.info("eventName: {} source: {} target: {}", eventName, source, target);
                    }
                });
    }

    @Override
    public void configure(StateMachineStateConfigurer<ApplicationReviewStates, ApplicationReviewEvents> states) throws Exception {
        states
                .withStates()
                .initial(ApplicationReviewStates.PEER_REVIEW)
                .state(ApplicationReviewStates.PRINCIPAL_REVIEW)
                .end(ApplicationReviewStates.APPROVED)
                .end(ApplicationReviewStates.REJECTED);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.PRINCIPAL_REVIEW).event(ApplicationReviewEvents.APPROVE)
                .and().withExternal()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.APPROVED).event(ApplicationReviewEvents.APPROVE)
                .and().withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT)
                .and().withExternal()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT);
    }
}