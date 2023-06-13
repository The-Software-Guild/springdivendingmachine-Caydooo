package com.vending.dao;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.List;

public interface VendingDAO {

	public void loadAll(ObjectInputStream ois);

	public void saveAll(ObjectOutputStream oos);

	public void retrieveAll();

	public boolean checkItemCode(int response);

	public boolean checkSufficientFunds(int response, BigDecimal cashAmount);

	public void vendItem(int response);

	public BigDecimal provideChange(int response, BigDecimal cashAmount);

	public List<String> calculateChange(BigDecimal change);

}
