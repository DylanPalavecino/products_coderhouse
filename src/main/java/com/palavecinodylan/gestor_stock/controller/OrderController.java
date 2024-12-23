package com.palavecinodylan.gestor_stock.controller;

import com.palavecinodylan.gestor_stock.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/empty_order")
    public ResponseEntity<?> createEmptyOrder() {
        return ResponseEntity.ok(orderService.newEmptyOrder());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @DeleteMapping("/remove_item/{orderId}/{itemId}")
    public ResponseEntity<?> deleteItemFromOrder(@PathVariable Long orderId,@PathVariable Long itemId) throws Exception {
        orderService.deleteItemFromOrder(orderId, itemId);
        return ResponseEntity.ok().build();
    }


}