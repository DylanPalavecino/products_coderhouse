package com.palavecinodylan.gestor_stock.service.impl;

import com.palavecinodylan.gestor_stock.controller.request.OrderItemRequest;
import com.palavecinodylan.gestor_stock.dto.OrderItemDTO;
import com.palavecinodylan.gestor_stock.entity.OrderEntity;
import com.palavecinodylan.gestor_stock.entity.OrderItemEntity;
import com.palavecinodylan.gestor_stock.entity.ProductEntity;
import com.palavecinodylan.gestor_stock.exception.ObjectNotFoundException;
import com.palavecinodylan.gestor_stock.mapper.OrderItemEntityToDTO;
import com.palavecinodylan.gestor_stock.repository.OrderItemRepository;
import com.palavecinodylan.gestor_stock.repository.OrderRepository;
import com.palavecinodylan.gestor_stock.service.OrderItemService;
import com.palavecinodylan.gestor_stock.service.OrderService;
import com.palavecinodylan.gestor_stock.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemEntityToDTO orderItemEntityToDTO;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public OrderItemDTO addItem(OrderItemRequest orderItemRequest) throws Exception {

        ProductEntity product = productService.getProductEntityById(orderItemRequest.productId());
        OrderEntity order = orderService.getOrderEntityById(orderItemRequest.orderId());
        Double subtotal = product.getPrice() * orderItemRequest.quantity();

        if (orderItemRequest.quantity() > product.getStock()) {
            throw new ObjectNotFoundException("Not enough stock");
        }

        OrderItemEntity orderItem = OrderItemEntity.builder()
                .quantity(orderItemRequest.quantity())
                .order(order)
                .product(product)
                .subTotal(subtotal)
                .build();

        order.getOrderItems().add(orderItem);
        order.setTotalPrice(order.getTotalPrice() + subtotal);
        product.setStock(product.getStock() - orderItemRequest.quantity());
        orderRepository.save(order);
        return orderItemEntityToDTO.map(orderItem);

    }

    @Override
    public OrderItemEntity getItemEntity(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("OrderItem not found"));
    }



}


