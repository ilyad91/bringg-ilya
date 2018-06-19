package com.candidate.controllers;

import com.candidate.handlers.BringgOrderHandler;
import com.candidate.model.OrderDTO;
import com.candidate.model.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    BringgOrderHandler bringgOrderHandler;

    @PostMapping
    public Callable<ResponseEntity<OrderResponseDTO>> create(@RequestBody OrderDTO order) {
        return () -> ResponseEntity.ok(bringgOrderHandler.handleOrder(order));

    }

}
