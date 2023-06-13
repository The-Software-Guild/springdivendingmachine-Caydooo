package com.vending.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.vending.service.VendingService;

public class Controller {

	/*
		-----------------------------------------------------------------------------------------------------------------------------------------------------
		Requirements: DONE
		
		done - The program should display all of the items and their respective prices when the program starts, along with an option to exit the program.
		done - The user must put in some amount of money before an item can be selected.
		done - Only one item can be vended at a time.
		done - If the user selects an item that costs more than the amount the user put into the vending machine, the program should display a message
		indicating insufficient funds and then redisplay the amount the user had put into the machine.
		done - If the user selects an item that costs equal to or less than the amount of money that the user put in the vending machine, the program
		should display the change returned to the user. Change must be displayed as the number of dollars and cents returned to the user.
		done - Vending machine items must be stored in a file. Inventory for the vending machine must be read from this file when the program starts
		and must be written out to this file just before the program exits. The program must track the following properties for each item:
			done - Item name
			done - Item cost
			done - Number of items in inventory
		done - When an item is vended, the program must update the inventory level appropriately. If the machine runs out of an item, it should no longer
		be available as an option to the user. However, the items that have an inventory level of zero must still be read from and written to the
		inventory file and stored in memory.
	
		-----------------------------------------------------------------------------------------------------------------------------------------------------
		Guidelines:
		
		- You must have a full set of unit tests for your DAO and Service Layer components.
		- Include an Audit DAO to log events and the time they occurred.
		
		done - You should take considerable time to design this application before you even think about writing code. Follow the Service Layer
		and DAO interface design approaches shown in the write-ups and videos.
		done - This application must follow the MVC pattern used for all previous labs (App class, Controller, View, Service Layer, DAO) – this
		includes the use of constructor-based dependency injection.
		done - You must use BigDecimal for all monetary calculations where applicable.
		- You must include at least one lambda function in the solution.
		done - You must use application-specific exceptions and your application must fail gracefully under all conditions (i.e. no displaying
		a stack trace when an exception is thrown). At a minimum you should have the following application-specific exceptions thrown by
		your Service Layer:
			done - One that is thrown when the user tries to purchase an item but doesn't deposit enough money (i.e. InsufficientFundsException)
			done - One that is thrown when the user tries to purchase an item that has zero inventory (i.e. NoItemInventoryException)
		done - Use enums to represent the values of different coins.
		
		-----------------------------------------------------------------------------------------------------------------------------------------------------
		Additionally:
		
		done - Convert the Vending Machine application you created in an earlier activity to use the Spring DI container. Use the lesson notes
		as a guide and pattern your approach after the Class Roster DI Conversion Code Along.
		
		-----------------------------------------------------------------------------------------------------------------------------------------------------
	*/
	
	public static void main(String[] args) {
		System.out.println("Welcome to the Java Vending Machine");
		ApplicationContext context = new ClassPathXmlApplicationContext("vending-bean.xml");
		VendingService vService = (VendingService) context.getBean("vService", VendingService.class);
		
		// load items into vending machine
		vService.loadVendingMachine();
		
		// run vending machine
		vService.run();
		
		// close vending machine and save items and counts
		vService.saveVendingMachine();
		
	}
}








