package com.example.mscart.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Long id) {super(message + " " + id);
    }

    public NotFoundException(String message, UUID eventId) {super(message + " " + eventId);
    }

    public NotFoundException(String message, String name) {super(message + " " + name);
    }

    public NotFoundException(String message) {super(message);
    }
}
