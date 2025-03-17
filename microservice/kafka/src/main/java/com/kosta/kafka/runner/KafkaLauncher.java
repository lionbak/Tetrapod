package com.kosta.kafka.runner;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class KafkaLauncher implements DisposableBean {

    private final KafkaServerHandler kafkaServerHandler;
    private final ZookeeperHandler zookeeperHandler;
    private final KafkaConnectorHandler kafkaConnectorHandler;

    public KafkaLauncher(KafkaServerHandler kafkaServerHandler, ZookeeperHandler zookeeperHandler, KafkaConnectorHandler kafkaConnectorHandler) {
        this.kafkaServerHandler = kafkaServerHandler;
        this.zookeeperHandler = zookeeperHandler;
        this.kafkaConnectorHandler = kafkaConnectorHandler;

        startCluster();
    }

    private void startCluster() {
        try{
            zookeeperHandler.startZookeeper(); // zookeeper 시작

            if(zookeeperHandler.isRunning()) { // zookeeper running true 확인시 kafka server 실행
                kafkaServerHandler.startKafkaServer();
                if (kafkaServerHandler.isRunning()) {
                    kafkaConnectorHandler.startKafkaConnector();
                } else {
                    System.out.println("Kafka Server is not running");
                }
            } else {
                System.out.println("Zookeeper Server is not running");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try{
            System.out.println("Kafka Connector stopped");
            kafkaConnectorHandler.stopKafkaConnector();

            System.out.println("Kafka Server stopped");
            kafkaServerHandler.stopKafkaServer();

            System.out.println("Zookeeper Server stopped");
            zookeeperHandler.stopZookeeperServer();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
