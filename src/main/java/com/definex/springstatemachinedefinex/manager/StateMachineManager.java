package com.definex.springstatemachinedefinex.manager;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

import java.util.Map;

@Log4j2
public final class StateMachineManager {

    public static void sendEvent(StateMachine<String, String> stateMachine, String payload) {
        Message<String> message = MessageBuilder.withPayload(payload)
                .build();

        sendEvent(stateMachine, message);
    }

    public static void sendEvent(StateMachine<String, String> stateMachine, String payload, Map<String,Object> dataMap) {
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader("data", dataMap)
                .build();

        sendEvent(stateMachine, message);
    }

    private static void sendEvent(StateMachine<String, String> stateMachine, Message<String> message) {

        stateMachine.sendEvent(Mono.just(message)).subscribe(
                result -> log.info("Result: " + result),
                error -> {
                    log.error("Error: " + error);
                    throw new RuntimeException(error);
                },
                () -> log.info("State Machine Completed")
        );
    }
}
