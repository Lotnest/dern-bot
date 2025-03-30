package dev.lotnest.dernbot;

import dev.lotnest.dernbot.jda.bot.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DernBot {
    public static void main(String... args) {
        SpringApplication.run(DernBot.class, args);
    }

    @Component
    @RequiredArgsConstructor
    public static class BotInitializer {
        private final Bot bot;

        @Bean
        public ApplicationRunner runner() {
            return args -> {
                new Thread(bot::run, "dern-bot").start();
                Runtime.getRuntime().addShutdownHook(new Thread(bot::shutdownHook));
            };
        }
    }
}
