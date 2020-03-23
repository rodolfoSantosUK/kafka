package br.com.alura.ecommerce.domain;

import java.math.BigDecimal;

public class Order {

    private final String userId, orderId;
    private final BigDecimal amout;

    public Order(String userId, String orderId, BigDecimal amout) {
        this.userId = userId;
        this.orderId = orderId;
        this.amout = amout;
    }
}
