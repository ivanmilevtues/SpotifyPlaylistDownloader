package com.spd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class SpotifyPlaylistDownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotifyPlaylistDownloaderApplication.class, args);
	}
}
