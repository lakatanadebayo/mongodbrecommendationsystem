package com.mongodbrecommendationsystem.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Configuration
public class SbertProcessManager {

    private Process process;

    @PostConstruct
    public void startSbertApi() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "uvicorn",
                    "sbert_api:app",
                    "--host", "0.0.0.0",
                    "--port", "8000"
            );

            pb.directory(new File("mongodbrecommendationsystem\\server_sbert"));
            pb.redirectErrorStream(true);

            process = pb.start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[SBERT] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException("Erreur démarrage SBERT API", e);
        }
    }

    @PreDestroy
    public void stopSbertApi() {
        if (process != null && process.isAlive()) {
            process.destroy();
            System.out.println("SBERT API arrêtée proprement.");
        }
    }
}
