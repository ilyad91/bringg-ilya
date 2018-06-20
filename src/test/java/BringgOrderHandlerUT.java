import com.candidate.handlers.BringgOrderHandler;
import com.candidate.model.OrderDTO;
import com.candidate.model.OrderResponseDTO;
import com.candidate.services.BringgService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
public class BringgOrderHandlerUT {

    @TestConfiguration
    static class BringgOrderHandlerImplTestContextConfiguration {

        @Bean
        public BringgService bringgService() {
            return new BringgService();
        }

        @Bean
        public BringgOrderHandler bringgOrderHandler() {
            return new BringgOrderHandler();
        }
    }

    @Autowired
    private BringgOrderHandler bringgOrderHandler;

    @Test
    public void testGetOrdersFromPastWeek_checkThatAllWereCreatedWithinThePastWeek() throws UnsupportedEncodingException, UnirestException {
        ZonedDateTime dateOneWeekAgoUTC = Instant.now().minus(Duration.ofDays(7)).atZone(ZoneId.of("UTC"));

        List<OrderDTO> bringgOrdersFromPastWeek = bringgOrderHandler.getPastWeekOrders();

        for (OrderDTO orderDTO : bringgOrdersFromPastWeek){
            ZonedDateTime orderCreatedAt = ZonedDateTime.parse(orderDTO.getCreated_at());
            Assert.assertTrue(orderCreatedAt.isAfter(dateOneWeekAgoUTC));
        }
    }

    @Test
    public void testCreateOrder() throws UnsupportedEncodingException, UnirestException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setName("ilya_test");
        orderDTO.setCell_number("0545555555");
        orderDTO.setAddress("tel aviv test");
        orderDTO.setOrder_details("amazing test chair");

        OrderResponseDTO orderResponseDTO = bringgOrderHandler.createOrder(orderDTO);

        Assert.assertEquals(true, orderResponseDTO.isSuccess());
        Assert.assertEquals("ilya_test", orderResponseDTO.getOrder().getName());
        Assert.assertEquals("0545555555", orderResponseDTO.getOrder().getCell_number());
        Assert.assertEquals("tel aviv test", orderResponseDTO.getOrder().getAddress());
        Assert.assertEquals("amazing test chair", orderResponseDTO.getOrder().getOrder_details());
        Assert.assertTrue(!orderResponseDTO.getOrder().getCreated_at().isEmpty());
    }

}
