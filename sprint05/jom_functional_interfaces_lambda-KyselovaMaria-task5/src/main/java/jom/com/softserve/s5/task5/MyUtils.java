package jom.com.softserve.s5.task5;

import java.util.function.Predicate;
import java.util.Set;

class MyUtils {

    public static Predicate<Integer> getPredicateFromSet(Set<Predicate<Integer>> set) {
        Predicate<Integer> result = value -> true;
        for (Predicate<Integer> predicate: set){
            result = result.and(predicate);
        }
        return result;
    }

}
