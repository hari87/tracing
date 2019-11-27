package msp.device.backup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.sql.Timestamp;

public class CustomerItemProcessor implements ItemProcessor<CustomerIds, CustomerIds> {
    Logger logger = LoggerFactory.getLogger(CustomerItemProcessor.class);

    @Override
    public CustomerIds process(CustomerIds customerIds) throws Exception {
        logger.info("inside processor");
        return customerIds;
    }
}
