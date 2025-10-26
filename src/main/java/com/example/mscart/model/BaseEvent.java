package com.example.mscart.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEvent<T extends Serializable> implements Serializable {
    private String eventId;

    private T payload;
}
