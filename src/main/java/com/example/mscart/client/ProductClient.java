package com.example.mscart.client;

import com.example.mscart.config.FeignClientConfig;
import com.example.mscart.model.response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product",
        url = "${client.catalog-service.url}",
        configuration = FeignClientConfig.class)
public interface ProductClient {
    @GetMapping("/{id}")
    ProductResponseDto getById(@PathVariable Long id);
}
