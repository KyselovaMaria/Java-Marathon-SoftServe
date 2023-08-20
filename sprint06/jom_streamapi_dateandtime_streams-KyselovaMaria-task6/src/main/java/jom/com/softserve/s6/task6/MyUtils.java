package jom.com.softserve.s6.task6;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MyUtils {
    public Map<String, Stream<String>> phoneNumbers(List<Stream<String>> list) {
        return list.stream()
                .flatMap(s -> s)
                .filter(s -> (s != null) && (!s.isEmpty()))
                .map(s -> s.replaceAll("[^\\d]", ""))
                .map(s -> s.length() == 7 ? "loc".concat(s) : s.length() < 7 ? "err".concat(s) : s)
                .distinct()
                .collect(Collectors.groupingBy(s -> s.substring(0, 3)))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(s -> s.substring(3))
                                .sorted()));
    }
}

