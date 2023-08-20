package jom.com.softserve.s5.task3;

import java.util.function.BinaryOperator;
import java.util.ArrayList;
import java.util.List;
public class App {

	public static List<String> createGreetings(List<Person> personList, BinaryOperator<String> operator) {
		List<String> greetings = new ArrayList<>();
		for (Person person : personList) {
			String greeting = operator.apply(person.name, person.surname);
			greetings.add(greeting);
		}
		return greetings;
	}

	public static final BinaryOperator<String> greetingOperator = (s1, s2) -> "Hello " + s1 + " " + s2 + "!!!";
}

