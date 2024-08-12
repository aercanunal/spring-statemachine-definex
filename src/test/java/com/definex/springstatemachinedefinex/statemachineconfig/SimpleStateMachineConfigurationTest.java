package com.definex.springstatemachinedefinex.statemachineconfig;

import com.definex.springstatemachinedefinex.manager.StateMachineManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleStateMachineConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleStateMachineConfigurationTest {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine.startReactively().block();
    }

    @Test
    @Order(1)
    public void whenSimpleStringStateMachineEvents_thenEndState() {
        assertEquals("SI", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"E1");
        assertEquals("S1", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"E2");
        assertEquals("S2", stateMachine.getState().getId());
    }

    @Test
    @Order(2)
    public void whenSimpleStringMachineActionState_thenActionExecuted() {

        StateMachineManager.sendEvent(stateMachine,"E3");
        assertEquals("S3", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"E4");

        assertEquals("S4", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"E5");
        assertEquals("S5", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"end");
        assertEquals("SF", stateMachine.getState().getId());

        assertEquals(2, stateMachine.getExtendedState().getVariables().get("approvalCount"));
    }

    @AfterEach
    public void tearDown() {
        stateMachine.stopReactively().block();
    }

}