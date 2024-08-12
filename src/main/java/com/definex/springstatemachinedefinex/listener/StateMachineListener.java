package com.definex.springstatemachinedefinex.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Log4j2
public class StateMachineListener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        log.info("***-> StateChanged from: {} to: {}", getStateId(from), getStateId(to));
    }


    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        log.info("***-> State Machine Stage: " + stateContext.getStage());
    }

    private static String getStateId(State<String, String> state) {
        return state != null ? state.getId() : " - ";
    }
}
