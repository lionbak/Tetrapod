package com.kosta.kafka.runner;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ZookeeperHandler {

    private Process process;
    private boolean running = false;

    public void startZookeeper() {
        String kafkaDir = "D:\\kafka\\kafka_2.12-3.8.0";
        String zookeeperScript = kafkaDir + "\\bin\\windows\\zookeeper-server-start.bat";
        String zookeeperConfig = kafkaDir + "\\config\\zookeeper.properties";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", zookeeperScript, zookeeperConfig
        );
        processBuilder.redirectErrorStream(true);

        System.out.println(processBuilder.command());
        try {
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
                    if(line.contains("ZooKeeper audit is disabled.")){
                        System.out.println("Zookeeper has started successfully");
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
            thread.join(20000); // 스레드가 완료될 때까지 기다림
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    @PreDestroy
    public void stopZookeeperServer() throws IOException {
        String kafkaDir = "D:\\kafka\\kafka_2.12-3.8.0";
        String zookeeperScript = kafkaDir + "\\bin\\windows\\zookeeper-server-stop.bat";
        String zookeeperConfig = kafkaDir + "\\config\\zookeeper.properties";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", zookeeperScript, zookeeperConfig
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
