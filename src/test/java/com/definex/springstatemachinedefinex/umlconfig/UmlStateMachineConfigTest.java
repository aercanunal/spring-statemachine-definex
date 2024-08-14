package com.definex.springstatemachinedefinex.umlconfig;

import com.definex.springstatemachinedefinex.manager.StateMachineManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UmlStateMachineConfig.class)
public class UmlStateMachineConfigTest {

    @Autowired
    private StateMachineFactory<String,String> stateMachineFactory;

    @Test
    public void createStateMachine() {
        assertNotNull(stateMachineFactory.getStateMachine(UUID.randomUUID().toString()));
    }

    @Test
    public void whenSimpleStringStateMachineEvents_thenEndState() {
        StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine();

        stateMachine.getExtendedState().getVariables().put("validation", false);

        assertEquals("STARTUP", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"START");
        assertEquals("PREORDER", stateMachine.getState().getId());

        StateMachineManager.sendEvent(stateMachine,"ORDER", Map.of("count",5L));
        assertEquals("PREORDER", stateMachine.getState().getId());

        stateMachine.getExtendedState().getVariables().put("validation", true);
        StateMachineManager.sendEvent(stateMachine,"ORDER", Map.of("count",6L));
        assertEquals("FINAL", stateMachine.getState().getId());
    }

}