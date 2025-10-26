package com.example.mscart.client.decoder;

import com.example.mscart.exception.NotFoundException;
import com.example.mscart.exception.OrderServiceUnavailableException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorDecoder implements feign.codec.ErrorDecoder {
    private final feign.codec.ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign error: method={}, status={}, reason={}",
                methodKey, response.status(), response.reason());

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException("Invalid request to Order service");
            case 404 -> new NotFoundException("Product not found");
            case 500, 502, 503, 504 ->
                    new OrderServiceUnavailableException("Order service is temporarily unavailable");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
