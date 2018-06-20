package com.candidate.controllers;

import com.candidate.handlers.BringgOrderHandler;
import com.candidate.model.OrderDTO;
import com.candidate.model.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    BringgOrderHandler bringgOrderHandler;

    @PostMapping
    public Callable<ResponseEntity<OrderResponseDTO>> create(@RequestBody OrderDTO order) {
        return () -> ResponseEntity.ok(bringgOrderHandler.createOrder(order));
    }

    @GetMapping
    public Callable<ResponseEntity<List<OrderDTO>>> getPassingWeekOrders() {
        return () -> ResponseEntity.ok(bringgOrderHandler.getPastWeekOrders());
    }
}
