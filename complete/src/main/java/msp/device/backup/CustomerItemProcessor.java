package msp.device.backup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.sql.Timestamp;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {
    Logger logger = LoggerFactory.getLogger(CustomerItemProcessor.class);
    Timestamp currentTs = new Timestamp(System.currentTimeMillis());

    @Override
    public Customer process(Customer customerIds) throws Exception {
        logger.info("inside processor");
        return new Customer(customerIds.getCUSTOMER_IDENTIFIER(), currentTs,currentTs, "hrameshrao","hrameshrao");
    }
}
