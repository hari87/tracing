package msp.device.backup;

import com.example.batchprocessing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

//@Component
public class JobCompletionNotifer extends JobExecutionListenerSupport {
    Logger logger = LoggerFactory.getLogger(JobCompletionNotifer.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotifer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT * FROM customer",
                    (rs, row) -> new Customer(
                            rs.getInt(1),rs.getTimestamp(2),rs.getTimestamp(3),rs.getString(4),rs.getString(5))
            ).forEach(person -> logger.info("Found <" + person + "> in the database."));
        }
    }
}
