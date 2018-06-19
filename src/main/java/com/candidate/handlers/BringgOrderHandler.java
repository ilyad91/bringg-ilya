package com.candidate.handlers;

import com.candidate.bringg_model.BringgCustomerDTO;
import com.candidate.bringg_model.BringgCustomerResponseDTO;
import com.candidate.bringg_model.BringgOrderDTO;
import com.candidate.bringg_model.BringgOrderResponseDTO;
import com.candidate.model.OrderDTO;
import com.candidate.model.OrderResponseDTO;
import com.candidate.services.BringgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BringgOrderHandler implements OrderHandler {

    @Autowired
    private BringgService bringgService;

    @Override
    public OrderResponseDTO handleOrder(OrderDTO order) throws Exception{
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrder(order);

        BringgCustomerDTO bringgCustomerDTO = extractBringgCustomerDTO(order);
        BringgCustomerResponseDTO bringgCustomerResponseDTO = bringgService.createCustomer(bringgCustomerDTO);
        int customerId = bringgCustomerResponseDTO.getBringgCustomerDTO().getId();

        BringgOrderDTO bringgOrderDTO = new BringgOrderDTO();
        bringgOrderDTO.setCustomer_id(customerId);
        BringgOrderResponseDTO bringgOrderResponseDTO = bringgService.createOrder(bringgOrderDTO);

        orderResponseDTO.setSuccess(true);
        return orderResponseDTO;
    }

    private BringgCustomerDTO extractBringgCustomerDTO(OrderDTO orderDTO) {
        BringgCustomerDTO bringgCustomerDTO = new BringgCustomerDTO();
        bringgCustomerDTO.setName(orderDTO.getName());
        bringgCustomerDTO.setPhone(orderDTO.getCell_number());
        bringgCustomerDTO.setAddress(orderDTO.getAddress());

        return bringgCustomerDTO;
    }
}
