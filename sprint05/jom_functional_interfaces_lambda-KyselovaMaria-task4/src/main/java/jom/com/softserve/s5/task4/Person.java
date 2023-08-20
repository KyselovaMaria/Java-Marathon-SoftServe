package jom.com.softserve.s5.task4;

import java.util.ArrayList;
import java.util.List;

interface DecisionMethod {
	boolean decide(String productName, int discount);
}

class Person {
	String name;
	DecisionMethod goShopping;

	Person(String name) {
		this.name = name;
		this.goShopping = (productName, discount) -> {
			if (productName.equals("product1") && discount > 10) {
				return true;
			} else {
				return false;
			}
		};
	}
}

class Shop {
	public List<DecisionMethod> clients = new ArrayList<>();

	public int sale(String product, int percent) {
		int count = 0;
		for (DecisionMethod decisionMethod : clients) {
			if (decisionMethod.decide(product, percent)) {
				count++;
			}
		}
		return count;
	}
}
