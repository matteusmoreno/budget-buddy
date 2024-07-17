package com.matteusmoreno.budget_buddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BudgetBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetBuddyApplication.class, args);
	}

}
