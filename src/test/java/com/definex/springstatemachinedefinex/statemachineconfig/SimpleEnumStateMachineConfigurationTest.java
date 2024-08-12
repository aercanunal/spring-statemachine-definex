package com.definex.springstatemachinedefinex.statemachineconfig;

import com.definex.springstatemachinedefinex.applicationreview.ApplicationReviewEvents;
import com.definex.springstatemachinedefinex.applicationreview.ApplicationReviewStates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleEnumStateMachineConfiguration.class)
public class SimpleEnumStateMachineConfigurationTest {


    @Autowired
    private StateMachine<ApplicationReviewStates, ApplicationReviewEvents> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine.startReactively().block();
    }


    @Test
    public void whenStateMachineConfiguredWithEnums_thenStateMachineAcceptsEnumEvents() {
        Message<ApplicationReviewEvents> approveMessage = MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build();
        stateMachine.sendEvent(Mono.just(approveMessage)).subscribe(
                Assertions::assertNotNull,
                error -> Assertions.fail(),
                () -> assertEquals(ApplicationReviewStates.PRINCIPAL_REVIEW, stateMachine.getState().getId())
        );

        Message<ApplicationReviewEvents> rejectMessage = MessageBuilder.withPayload(ApplicationReviewEvents.REJECT).build();

        stateMachine.sendEvent(Mono.just(rejectMessage)).subscribe(
                Assertions::assertNotNull,
                error -> Assertions.fail(),
                () -> assertEquals(ApplicationReviewStates.REJECTED, stateMachine.getState().getId())
        );

    }

    @AfterEach
    public void tearDown() {
        stateMachine.stopReactively().block();
    }

}