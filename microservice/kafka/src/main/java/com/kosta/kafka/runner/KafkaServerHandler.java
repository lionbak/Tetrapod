package com.kosta.kafka.runner;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaServerHandler {

    private Process process;
    private boolean running = false;

    public void startKafkaServer() {
        String kafkaDir = "D:\\kafka\\kafka_2.12-3.8.0";
        String kafkaScript = kafkaDir + "\\bin\\windows\\kafka-server-start.bat";
        String kafkaConfig = kafkaDir + "\\config\\server.properties";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", kafkaScript, kafkaConfig
        );
        processBuilder.redirectErrorStream(true);

        System.out.println(processBuilder.command());
        try{
            process = processBuilder.start();
            printProcessOutputAndWait(process);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void printProcessOutputAndWait(Process process) {
        Thread thread = new Thread(() -> {
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))){
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    //zookeeper 시작 확인 문자열
                    if(line.contains("[KafkaServer id=0] started")){
                        System.out.println("KafkaServer has started successfully");
                        running = true; // zookeeper 시작 -> Launcher에 true 전달
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        thread.start();

        try {
            thread.join(); // 스레드가 완료될 때까지 기다림
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    @PreDestroy
    public void stopKafkaServer() throws IOException {
        String kafkaDir = "D:\\kafka\\kafka_2.12-3.8.0";
        String kafkaServerScript = kafkaDir + "\\bin\\windows\\kafka-server-stop.bat";
        String kafkaServerConfig = kafkaDir + "\\config\\server.properties";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", kafkaServerScript, kafkaServerConfig
        );
        processBuilder.redirectErrorStream(true);

        System.out.println(processBuilder.command());
        try {
            process = processBuilder.start();
            printProcessOutputAndWait(process);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
