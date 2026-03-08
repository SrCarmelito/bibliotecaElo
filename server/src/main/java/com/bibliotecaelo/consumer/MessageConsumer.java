package com.bibliotecaelo.consumer;

import lombok.Data;

@Data
public class MessageConsumer {

    private EntityEvent entityEvent;
    private String payload;

}
