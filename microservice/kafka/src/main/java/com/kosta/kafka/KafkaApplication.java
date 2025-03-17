package com.kosta.kafka;

import com.kosta.kafka.runner.KafkaLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EntityScan(basePackages = {"com.kosta.kafka.entity"})
public class KafkaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);

        KafkaLauncher launcher = context.getBean(KafkaLauncher.class);
    }

}
