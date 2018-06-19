package com.candidate.handlers;

import com.candidate.model.OrderDTO;

public interface OrderHandler {

    /**
     * @param order
     * @return true on successful handling of the order, false otherwise
     */
    boolean handleOrder(OrderDTO order);
}
