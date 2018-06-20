import com.candidate.bringg_model.BringgOrderDTO;
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
import java.util.List;

@RunWith(SpringRunner.class)
public class BringgServiceUT {

    @TestConfiguration
    static class BringgServiceImplTestContextConfiguration {

        @Bean
        public BringgService bringgService() {
            return new BringgService();
        }
    }

    @Autowired
    private BringgService bringgService;

    @Test
    public void testGetOrders() throws UnsupportedEncodingException, UnirestException {
        List<BringgOrderDTO> bringgOrderDTOList = bringgService.getOrders(0);

        Assert.assertTrue(bringgOrderDTOList.size() > 0);
    }
}
