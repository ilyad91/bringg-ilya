package com.candidate.handlers;

import com.candidate.model.OrderDTO;
import com.candidate.model.OrderResponseDTO;

public interface OrderHandler {

    /**
     * @param order
     * @return response on successful handling of the order, null otherwise
     */
    OrderResponseDTO handleOrder(OrderDTO order) throws Exception;
}
