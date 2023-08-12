package com.softserve.edu.sprint4.task7;

import java.lang.reflect.Method;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;



class ClassForAnnot {
	@CamelCase
	public static void example() {
	}

	@CamelCase
	public void Example() {
	}
}

class Class1 {
	@CamelCase
	public void correct() {
	}

	@CamelCase
	public void InCorrect() {
	}

	@CamelCase
	public void JustMethod() {
	}
}

class Class2 {
	@CamelCase
	public void correct() {
	}

	@CamelCase
	public void oneMoreCorrect() {
	}
}

public class CheckCamelCase {
	public final static String CAMELCASE_PATTERN = "[a-z][a-zA-Z0-9]*";
	public final static String ERROR_MESSAGE_TEMPLATE = "method %s.%s doesn't satisfy camelCase naming convention\n";

	public static boolean checkAndPrint(Class<?> clazz) {
		boolean allMethodsSatisfyPattern = true;

		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(CamelCase.class)) {
				String methodName = method.getName();
				if (!Pattern.matches(CAMELCASE_PATTERN, methodName)) {
					allMethodsSatisfyPattern = false;
					System.out.printf(ERROR_MESSAGE_TEMPLATE, clazz.getSimpleName(), methodName);
				}
			}
		}

		return allMethodsSatisfyPattern;
	}

	public static boolean isConstantPatternPresent() {
		try {
			Field patternField = CheckCamelCase.class.getDeclaredField("CAMELCASE_PATTERN");
			return patternField != null && Modifier.isStatic(patternField.getModifiers())
					&& Modifier.isFinal(patternField.getModifiers()) && patternField.getType() == String.class;
		} catch (NoSuchFieldException e) {
			return false;
		}
	}
}
