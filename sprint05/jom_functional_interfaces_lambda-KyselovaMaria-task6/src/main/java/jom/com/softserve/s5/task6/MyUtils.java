package jom.com.softserve.s5.task6;

import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.List;

class MyUtils {
	public static int findMaxByCondition(List<Integer> numbers, Predicate<Integer> condition) {
		int max = Integer.MIN_VALUE;
		for (int number : numbers) {
			if (condition.test(number) && number > max) {
				max = number;
			}
		}
		return max;
	}
}

class User {
	public final List<Integer> values = new ArrayList<Integer>();

	int getFilterdValue(BiFunction<List<Integer>, Predicate<Integer>, Integer> listPredicateIntegerBiFunction, Predicate<Integer> filter) {
		return listPredicateIntegerBiFunction.apply(values, filter);
	}

	int getMaxValueByCondition(Predicate<Integer> predicate) {
		return getFilterdValue(MyUtils::findMaxByCondition, predicate);
	}
}
