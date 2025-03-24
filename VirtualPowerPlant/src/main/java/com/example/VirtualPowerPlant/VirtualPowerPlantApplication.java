package com.example.VirtualPowerPlant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VirtualPowerPlantApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {

		SpringApplication.run(VirtualPowerPlantApplication.class, args);
	}

	// List all beans to see if ModelMapper is included
	@Override
	public void run(String... args) throws Exception {
//		String[] beanNames = applicationContext.getBeanDefinitionNames();
//		System.out.println("Beans in the Spring context:");
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
	}

}
