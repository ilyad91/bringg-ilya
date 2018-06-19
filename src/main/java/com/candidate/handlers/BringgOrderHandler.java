package com.candidate.handlers;

import com.candidate.bringg_model.BringgCustomerDTO;
import com.candidate.model.OrderDTO;
import com.candidate.services.BringgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class BringgOrderHandler implements OrderHandler {

    @Autowired
    private BringgService bringgService;

    @Override
    public boolean handleOrder(OrderDTO order){
        BringgCustomerDTO bringgCustomerDTO = extractBringgCustomerDTO(order);

        try {
            return bringgService.createCustomer(bringgCustomerDTO);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    private BringgCustomerDTO extractBringgCustomerDTO(OrderDTO orderDTO) {
        BringgCustomerDTO bringgCustomerDTO = new BringgCustomerDTO();
        bringgCustomerDTO.setName(orderDTO.getName());
        bringgCustomerDTO.setPhone(orderDTO.getCell_number());
        bringgCustomerDTO.setAddress(orderDTO.getAddress());

        return bringgCustomerDTO;
    }
}
