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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ForkJoinStateMachineConfiguration.class)
public class ForkJoinStateMachineConfigurationTest {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine.startReactively().block();
    }

    @Test
    public void whenForkStateEntered_thenMultipleSubStatesEntered() {
        StateMachineManager.sendEvent(stateMachine, "E1");
        assertTrue(Arrays.asList("SFork", "Sub1-1", "Sub2-1").containsAll(stateMachine.getState().getIds()));
    }

    @Test
    public void whenAllConfiguredJoinEntryStatesAreEntered_thenTransitionToJoinState() {
        StateMachineManager.sendEvent(stateMachine, "E1");

        assertTrue(Arrays.asList("SFork", "Sub1-1", "Sub2-1").containsAll(stateMachine.getState().getIds()));

        StateMachineManager.sendEvent(stateMachine, "sub1");
        assertTrue(Arrays.asList("SFork", "Sub1-2", "Sub2-1").containsAll(stateMachine.getState().getIds()));
        StateMachineManager.sendEvent(stateMachine, "sub2");
        assertEquals("SJoin", stateMachine.getState().getId());
    }

    @AfterEach
    public void tearDown() {
        stateMachine.stopReactively().block();
    }
}
