package jom.com.softserve.s6.task7;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashSet;

public class MyUtils {
    public Stream<Integer> duplicateElements(Stream<Integer> stream) {
        HashSet<Integer> hashSet = new HashSet<>();
        return stream.filter(num -> num != null && !hashSet.add(num))
                .distinct()
                .sorted();
    }
}
