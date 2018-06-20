package com.candidate.handlers;

import com.candidate.bringg_model.BringgCustomerDTO;
import com.candidate.bringg_model.BringgCustomerResponseDTO;
import com.candidate.bringg_model.BringgOrderDTO;
import com.candidate.bringg_model.BringgOrderResponseDTO;
import com.candidate.model.OrderDTO;
import com.candidate.model.OrderResponseDTO;
import com.candidate.services.BringgService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class BringgOrderHandler implements OrderHandler {

    @Autowired
    private BringgService bringgService;

    @Override
    public OrderResponseDTO createOrder(OrderDTO order) throws UnsupportedEncodingException, UnirestException{
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrder(order);

        BringgCustomerDTO bringgCustomerDTO = extractBringgCustomerDTO(order);
        BringgCustomerResponseDTO bringgCustomerResponseDTO = bringgService.createCustomer(bringgCustomerDTO);
        int customerId = bringgCustomerResponseDTO.getCustomer().getId();

        BringgOrderDTO bringgOrderDTO = new BringgOrderDTO();
        bringgOrderDTO.setCustomer_id(customerId);
        BringgOrderResponseDTO bringgOrderResponseDTO = bringgService.createOrder(bringgOrderDTO);

        order.setCreated_at(bringgOrderResponseDTO.getTask().getCreated_at());
        orderResponseDTO.setSuccess(bringgOrderResponseDTO.isSuccess());
        return orderResponseDTO;
    }

    public List<OrderDTO> getPastWeekOrders() throws UnsupportedEncodingException, UnirestException {
        List<BringgOrderDTO> brinngOrdersFromPastWeek = new LinkedList<>();

        ZonedDateTime dateOneWeekAgoUTC = Instant.now().minus(Duration.ofDays(7)).atZone(ZoneId.of("UTC"));
        for(int page = 1;;page++){
            List<BringgOrderDTO> bringgOrderDTOList = bringgService.getOrders(page);
            int indexOfLastRelevantOrder = findOrdersAsOldAs(bringgOrderDTOList, dateOneWeekAgoUTC);

            if (indexOfLastRelevantOrder == bringgOrderDTOList.size() - 1){
                brinngOrdersFromPastWeek.addAll(bringgOrderDTOList);
            } else {
                brinngOrdersFromPastWeek.addAll(bringgOrderDTOList.subList(0, indexOfLastRelevantOrder));
                break;
            }
        }

        return convertBringgOrdersToOrdersDTO(brinngOrdersFromPastWeek);
    }

    private List<OrderDTO> convertBringgOrdersToOrdersDTO(List<BringgOrderDTO> bringgOrderDTOS) {
        List<OrderDTO> ordersDTOList = new LinkedList<>();

        for(BringgOrderDTO bringgOrderDTO : bringgOrderDTOS){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrder_details(bringgOrderDTO.getTitle());
            orderDTO.setCreated_at(bringgOrderDTO.getCreated_at());
            ordersDTOList.add(orderDTO);
        }

        return ordersDTOList;
    }

    private int findOrdersAsOldAs(List<BringgOrderDTO> bringgOrderDTOList, ZonedDateTime pastDate) {
        ZonedDateTime oldestOrderTime = ZonedDateTime.parse(bringgOrderDTOList.get(bringgOrderDTOList.size()-1).getCreated_at());
        if (oldestOrderTime.isAfter(pastDate)){
            return bringgOrderDTOList.size()-1;
        }

        int i = 0;
        for(; i<bringgOrderDTOList.size(); i++){
            String orderCreatedAtString = bringgOrderDTOList.get(i).getCreated_at();
            ZonedDateTime orderCreatedAt = ZonedDateTime.parse(orderCreatedAtString);

            if (orderCreatedAt.isBefore(pastDate)){
                break;
            }
        }
        return i;
    }

    private BringgCustomerDTO extractBringgCustomerDTO(OrderDTO orderDTO) {
        BringgCustomerDTO bringgCustomerDTO = new BringgCustomerDTO();
        bringgCustomerDTO.setName(orderDTO.getName());
        bringgCustomerDTO.setPhone(orderDTO.getCell_number());
        bringgCustomerDTO.setAddress(orderDTO.getAddress());

        return bringgCustomerDTO;
    }
}
