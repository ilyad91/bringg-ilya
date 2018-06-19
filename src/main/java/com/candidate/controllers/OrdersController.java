package com.candidate.controllers;

import com.candidate.handlers.BringgOrderHandler;
import com.candidate.model.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    BringgOrderHandler bringgOrderHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<OrderDTO>> create(@RequestBody OrderDTO order) {
        return () -> {
            boolean handleSuccessful = bringgOrderHandler.handleOrder(order);
            if (handleSuccessful){
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.badRequest().build();
            }
        };
    }

}
