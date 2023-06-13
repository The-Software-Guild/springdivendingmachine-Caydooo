package com.vending.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.vending.dao.VendingDAO;
import com.vending.dao.VendingDAOImp;
import com.vending.exceptions.InsufficientFundsException;
import com.vending.exceptions.InvalidAustralianAmountException;
import com.vending.exceptions.InvalidItemCodeException;


public class VendingServiceImp implements VendingService {

	@Autowired
	private VendingDAO vDao;
	private File itemStorage;
	private FileInputStream fis = null;
	private BufferedInputStream bis = null;
	private ObjectInputStream ois = null;
	private FileOutputStream fos = null;
	private BufferedOutputStream bos = null;
	private ObjectOutputStream oos = null;
	
	public static Scanner scanner = new Scanner(System.in);
	
	public VendingServiceImp() {
		itemStorage = new File("C://C353/Assignments/VendingMachine/ItemStorage.txt");
	}
	
	@Override
	public void loadVendingMachine() {
		try {
			fis = new FileInputStream(itemStorage);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			
			vDao.loadAll(ois);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		boolean cont = true;
		String amount = "";
		BigDecimal cashAmount = null;
		System.out.println("Displaying items...\n");
		
		while(cont) {
			cont = false;
			vDao.retrieveAll();
			System.out.print("\nPlease insert your money: ");
			
			try {
				amount = scanner.nextLine();
				System.out.println("amount: " + amount);
				cashAmount = new BigDecimal(amount);
				if(cashAmount.equals(new BigDecimal(0))) {
					System.out.println("Have a great day!");
					scanner.close();
					break;
				}
				BigDecimal validAmount = new BigDecimal("0.05");
				//System.out.println("cashAmount = " + cashAmount.remainder(new BigDecimal(0.05)).setScale(2, RoundingMode.HALF_UP));
				//System.out.println("cashAmount = " + cashAmount.remainder(new BigDecimal(0.05)).setScale(2, RoundingMode.HALF_UP).compareTo(validAmount));
				if(cashAmount.remainder(new BigDecimal(0.05)).setScale(2, RoundingMode.HALF_UP).compareTo(validAmount) != 0) {
					InvalidAustralianAmountException iaae = new InvalidAustralianAmountException();
					throw iaae;
				}
			} catch (NumberFormatException | InputMismatchException | ArithmeticException e) {
				System.out.println("This isn't a valid amount! please try again.\n");
				cont = true;
				continue;
			} catch (InvalidAustralianAmountException iaae) {
				System.out.println("This isn't a valid AUD amount! please try again.\n");
				cont = true;
				continue;
			}
			
			int response = 0;
			while(true) {
				System.out.print("\nPlease select an item with a corresponding integer value: ");
				response = 0;
				try {
					response = Integer.parseInt(scanner.nextLine());
					if(response == 0) {
						System.out.println("Refunding amount...");
						System.out.println("Have a great day!");
						cont = false;
						scanner.close();
						break;
					} else if(!vDao.checkItemCode(response)) {
						InvalidItemCodeException iice = new InvalidItemCodeException();
						throw iice;
					} else if(!vDao.checkSufficientFunds(response, cashAmount)) {
						InsufficientFundsException ife = new InsufficientFundsException();
						throw ife;
					}
					
					System.out.println("Vending item " + response + "...\n");
					vDao.vendItem(response);
					BigDecimal change = vDao.provideChange(response, cashAmount);
					
					List<String> changeList = vDao.calculateChange(change);
					
					System.out.print("Change provided: ");
					for(int i = 0; i < changeList.size(); i++) {
						if(i == changeList.size()-1) {
							System.out.println(changeList.get(i) + ".");
						} else {
							System.out.print(changeList.get(i) + ", ");
						}	
					}
					
					System.out.println("Total change: $" + change);
					System.out.println("Thank you!");
					return;
				} catch (NumberFormatException | InvalidItemCodeException e) {
					System.out.println("This isn't a valid item code! please try again.");
					continue;
				} catch (InsufficientFundsException e) {
					System.out.println("Insufficient funds! You only deposited $" + cashAmount + "."); 
					System.out.println("You didn't provide enough money to buy this item, please deposit more money.\n");
					cont = true;
					break;
				}
			}

		}
	}

	@Override
	public void saveVendingMachine() {
		try {
			fos = new FileOutputStream(itemStorage);
			bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);
			
			vDao.saveAll(oos);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
