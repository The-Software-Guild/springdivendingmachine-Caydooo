package com.vending.dao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.vending.dto.ItemDTO;

enum Coins {
	FIFTY_CENTS("Fifty cent", new BigDecimal("0.5")),
	TWENTY_CENTS("Twenty cent",new BigDecimal("0.2")),
	TEN_CENTS("Ten cent",new BigDecimal("0.1")),
	FIVE_CENTS("Five cent",new BigDecimal("0.05"));
	
	private String name;
	private BigDecimal amount;
	
	Coins(String coin_name, BigDecimal coin_amount){
		this.name = coin_name;
		this.amount = coin_amount;
	}
	
	public String getName() {
		return name;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
}

public class VendingDAOImp implements VendingDAO {

	private Map<Integer, ItemDTO> items;
	
	public VendingDAOImp() {
		items = new HashMap<>();
		
//		items.put(1, new ItemDTO("Orange Fanta 600ml", 3.5, 9));
//		items.put(2, new ItemDTO("Coca Cola 350ml", 2.3, 7));
//		items.put(3, new ItemDTO("Smith's Chicken Chips", 1.25, 2));
//		items.put(4, new ItemDTO("Cadbury Chocolate Bar", 0.8, 3));
//		items.put(5, new ItemDTO("Pepsi Max 600ml", 3.9, 6));

	}
	
	@Override
	public void loadAll(ObjectInputStream ois) {	
		Object obj;
		try {
			obj = ois.readObject();
			
			if(obj instanceof Map<?, ?>) {
				items = (Map<Integer, ItemDTO>) obj;
				//System.out.println(items);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveAll(ObjectOutputStream oos) {
		try {
			oos.writeObject(items);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void retrieveAll() {
		Set<Integer> itemCodes = items.keySet();
		Iterator<Integer> iter = itemCodes.iterator();
		
		while(iter.hasNext()) {
			int key = iter.next();
			ItemDTO item = items.get(key);
			
			if(item.getInvAmount() > 0) {
				System.out.println(key + ". " + item.toString());
			}
		}		
	}

	@Override
	public boolean checkItemCode(int response) {
		Set<Integer> itemSet = items.keySet();
		Iterator<Integer> iter = itemSet.iterator();
		
		while(iter.hasNext()) {
			int key = iter.next();
			if((key == response) && (items.get(key).getInvAmount() > 0)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean checkSufficientFunds(int response, BigDecimal cashAmount) {	
		ItemDTO item = items.get(response);
		BigDecimal cost = new BigDecimal(item.getCost());
		
		if(cost.compareTo(cashAmount) < 0) {
			return true;
		}
		return false;
	}

	@Override
	public void vendItem(int response) {
		ItemDTO item = items.get(response);
		item.setInvAmount(item.getInvAmount()-1);
	}

	@Override
	public BigDecimal provideChange(int response, BigDecimal cashAmount) {
		ItemDTO item = items.get(response);
		BigDecimal cost = new BigDecimal(item.getCost());
		
		BigDecimal change = cashAmount.subtract(cost);
		return change;
	}

	@Override
	public List<String> calculateChange(BigDecimal change) {
		List<String> changeList = new ArrayList<>();
		Map<Coins, Integer> changeMap = new TreeMap<>();
		
		int coinCounter = 0;
		boolean cont = true;
		
		//System.out.println("Change: " + change);
		while(cont) {
			if(change.compareTo(Coins.FIFTY_CENTS.getAmount()) >= 0) {
				change = change.subtract(Coins.FIFTY_CENTS.getAmount());
				coinCounter++;
				
				//System.out.println("Change here1: " + change);

			} else if (change.compareTo(Coins.FIFTY_CENTS.getAmount()) < 0) {
				changeMap.put(Coins.FIFTY_CENTS, coinCounter);
				break;
			} else if (change.compareTo(BigDecimal.ZERO) == 0) {
				cont = false;
				break;
			}
		}
		
		//System.out.println("Change: " + change);
		coinCounter = 0;
		while(cont) {
			if(change.compareTo(Coins.TWENTY_CENTS.getAmount()) >= 0) {
				change = change.subtract(Coins.TWENTY_CENTS.getAmount());
				coinCounter++;
				
				//System.out.println("Change here2: " + change);

			} else if (change.compareTo(Coins.TWENTY_CENTS.getAmount()) < 0) {
				changeMap.put(Coins.TWENTY_CENTS, coinCounter);
				break;
			} else if (change.compareTo(BigDecimal.ZERO) == 0) {
				cont = false;
				break;
			}
		}
		
		//System.out.println("Change: " + change);
		coinCounter = 0;
		while(cont) {
			if(change.compareTo(Coins.TEN_CENTS.getAmount()) >= 0) {
				change = change.subtract(Coins.TEN_CENTS.getAmount());
				coinCounter++;
				
				//System.out.println("Change here3: " + change);

			} else if (change.compareTo(Coins.TEN_CENTS.getAmount()) < 0) {
				changeMap.put(Coins.TEN_CENTS, coinCounter);
				break;
			} else if (change.compareTo(BigDecimal.ZERO) == 0) {
				cont = false;
				break;
			}
		}
		
		//System.out.println("Change: " + change);
		coinCounter = 0;
		while(cont) {
			if(change.compareTo(Coins.FIVE_CENTS.getAmount()) >= 0) {
				change = change.subtract(Coins.FIVE_CENTS.getAmount());
				coinCounter++;
				
				//System.out.println("Change here4: " + change);

			} else if (change.compareTo(Coins.FIVE_CENTS.getAmount()) < 0) {
				changeMap.put(Coins.FIVE_CENTS, coinCounter);
				break;
			} else if (change.compareTo(BigDecimal.ZERO) == 0) {
				cont = false;
				break;
			}
		}
		
		List<Coins> coinList = new ArrayList<Coins>(changeMap.keySet());
		Iterator<Coins> iter = coinList.iterator();
		
		while(iter.hasNext()) {
			Coins coin = iter.next();
			int amount = changeMap.get(coin);
			
			if(amount != 1) {
				changeList.add(amount + " " + coin.getName() + " coins");
			} else {
				changeList.add(amount + " " + coin.getName() + " coin");
			}
		}
		return changeList;
	}
}
