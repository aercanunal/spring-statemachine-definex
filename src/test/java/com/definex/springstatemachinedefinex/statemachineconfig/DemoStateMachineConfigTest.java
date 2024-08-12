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
@ContextConfiguration(classes = DemoStateMachineConfig.class)
class DemoStateMachineConfigTest {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine.start();
    }

    @Test
    public void demoSimpleState() {
        assertEquals("SI", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"E1");
        assertEquals("S1", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"E2");
        assertEquals("S2", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"end");
        assertEquals("SF", stateMachine.getState().getId());
    }

    @AfterEach
    public void tearDown() {
        stateMachine.stop();
    }
}