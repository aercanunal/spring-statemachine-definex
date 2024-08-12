package com.definex.springstatemachinedefinex.statemachineconfig;

import com.definex.springstatemachinedefinex.manager.StateMachineManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JunctionStateMachineConfiguration.class)
public class JunctionStateMachineConfigurationTest {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine.startReactively().block();
    }

    @Test
    public void whenTransitioningToJunction_thenArriveAtSubJunctionNode() {

        StateMachineManager.sendEvent(stateMachine, "E1");
        assertEquals("low", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine, "end");
        assertEquals("SF", stateMachine.getState().getId());
    }

    @AfterEach
    public void tearDown() {
        stateMachine.stopReactively().block();
    }

}