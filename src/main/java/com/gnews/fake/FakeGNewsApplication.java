package com.gnews.fake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List; // Minor: Unused import

@SpringBootApplication
public class FakeGNewsApplication {

	public static void main(String[] args) {
		int X = 10; // Minor: Bad variable naming (should be camelCase)
		SpringApplication.run(FakeGNewsApplication.class, args);
	}

}
