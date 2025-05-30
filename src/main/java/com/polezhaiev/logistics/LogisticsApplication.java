package com.polezhaiev.logistics;

import com.polezhaiev.logistics.frontend.MainFrame;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class LogisticsApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");

		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		ConfigurableApplicationContext context = SpringApplication.run(LogisticsApplication.class, args);

		SwingUtilities.invokeLater(() -> {
			MainFrame mainFrame = context.getBean(MainFrame.class);
			mainFrame.setVisible(true);
		});
	}
}
