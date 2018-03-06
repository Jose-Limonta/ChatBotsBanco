package com.sixdelta.telegrambot01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

//@SpringBootApplication
public class Telegrambot01Application {

	public static void main(String[] args) {
		// Initialize Api Context
		ApiContextInitializer.init();
		// Instantiate Telegram Bots API
		TelegramBotsApi botsApi = new TelegramBotsApi();
		// Register our bot
		try {
			botsApi.registerBot(new MyAmazingBot());
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Esponjatron con opciones de fotos y comandos ha sido iniciado! <<<<<<<<<<<<<<<<<<<<<<<<<");
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		//SpringApplication.run(Telegrambot01Application.class, args);
	}
}
