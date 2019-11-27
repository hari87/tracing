package msp.device.backup;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {
        Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Override
    public void setDataSource(DataSource dataSource) {
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database)
    }

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<CustomerIds> reader() {
        logger.info("inside file item reader");
        return new FlatFileItemReaderBuilder<CustomerIds>()
                .name("personItemReader")
                .resource(new ClassPathResource("customer-id.csv"))
                .delimited()
                .names(new String[]{"CUSTOMER_IDENTIFIER"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CustomerIds>() {{
                    setTargetType(CustomerIds.class);
                }})
                .build();
    }


    @Bean
    public CustomerItemProcessor processor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .flow(step1(writer()))
                .end()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerIds> writer() {
        logger.info("inside file JdbcbatchItemWriter ");
        return new JdbcBatchItemWriterBuilder<CustomerIds>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO customer (CUSTOMER_IDENTIFIER) VALUES (:CUSTOMER_IDENTIFIER)")
                .dataSource(dataSource())
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<CustomerIds> writer) {
        return stepBuilderFactory.get("step1")
                .<CustomerIds, CustomerIds> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@172.16.188.101:1521/J1CPEPE_srv");
        dataSource.setUsername("IE1_MSP_CPE_DEV");
        dataSource.setPassword("nexWA#j8hwVHg89Z");
        return dataSource;

    }



}
