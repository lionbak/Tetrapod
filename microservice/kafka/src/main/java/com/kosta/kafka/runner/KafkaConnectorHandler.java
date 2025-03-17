package com.kosta.kafka.runner;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaConnectorHandler {

    private Process process;

    public void startKafkaConnector() {
        String kafkaDir = "D:\\kafka\\confluent-7.5.5";
        String kafkaConnectorScript = kafkaDir + "\\bin\\windows\\connect-distributed.bat";
        String kafkaConnectorConfig = kafkaDir + "\\etc\\kafka\\connect-distributed.properties";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", kafkaConnectorScript, kafkaConnectorConfig
        );
        processBuilder.redirectErrorStream(true);

        System.out.println(processBuilder.command());
        try {
            process = processBuilder.start();
            printProcessOutput(process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printProcessOutput(Process process) {
        new Thread(() -> {
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))){
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @PreDestroy
    public void stopKafkaConnector() throws IOException {
        String kafkaDir = "D:\\kafka\\confluent-7.5.5";
        String kafkaConnectorScript = kafkaDir + "\\bin\\windows\\connect-server-stop.bat";
        String kafkaConnectorConfig = kafkaDir + "\\etc\\kafka\\connect-distributed.properties";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", kafkaConnectorScript, kafkaConnectorConfig
        );
        processBuilder.redirectErrorStream(true);

        System.out.println(processBuilder.command());
        try {
            process = processBuilder.start();
            printProcessOutput(process);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
