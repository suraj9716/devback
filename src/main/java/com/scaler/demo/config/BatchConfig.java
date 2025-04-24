package com.scaler.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.scaler.demo.entity.Product;
@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
    private DataSource dataSource;

    @Bean
    public FlatFileItemReader<Product> reader1() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names("productId", "title", "description", "price", "discount")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Product.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<Product, Product> itemProcessor() {
        System.out.println("inside item processor:::::::::::::::::");
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<Product> itemWriter() {
        System.out.println("inside item writer ::::::::::::::::::::;");
        return new JdbcBatchItemWriterBuilder<Product>()
                .sql("INSERT INTO products(product_id, title, description, price, discount, discounted_price) " +
                        "VALUES (:productId, :title, :description, :price, :discount, :discountedPrice)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepo,
                     PlatformTransactionManager transactionManager,
                     ItemReader<Product> reader,
                     ItemProcessor<Product, Product> processor,
                     ItemWriter<Product> writer) {
        return new StepBuilder("jobStep", jobRepo)
                .<Product, Product>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepo, Step step) {
        return new JobBuilder("importJob", jobRepo)
                .start(step)
                .build();
    }

    @Bean
    public ApplicationRunner jobRunner(JobLauncher jobLauncher, Job job) {
        return args -> {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // to ensure uniqueness
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        };
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

}
