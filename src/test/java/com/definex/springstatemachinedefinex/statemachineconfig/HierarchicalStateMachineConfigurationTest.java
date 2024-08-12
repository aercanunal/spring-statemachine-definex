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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HierarchicalStateMachineConfiguration.class)
public class HierarchicalStateMachineConfigurationTest {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine.startReactively().block();
    }


    @Test
    public void whenTransitionToSubMachine_thenSubStateIsEntered() {

        assertEquals(Arrays.asList("SI", "SUB1"), stateMachine.getState().getIds());

        StateMachineManager.sendEvent(stateMachine, "se1");
        assertEquals(Arrays.asList("SI", "SUB2"), stateMachine.getState().getIds());

        StateMachineManager.sendEvent(stateMachine, "s-end");
        assertEquals(Arrays.asList("SI", "SUBEND"), stateMachine.getState().getIds());

        StateMachineManager.sendEvent(stateMachine, "end");

        assertEquals(1, stateMachine.getState().getIds().size());
        assertEquals("SF", stateMachine.getState().getId());
    }

    @AfterEach
    public void tearDown() {
        stateMachine.stopReactively().block();
    }

}