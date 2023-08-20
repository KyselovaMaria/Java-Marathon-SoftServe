package jom.com.softserve.s6.task5;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;



public class MyUtils {
    public Stream<String> nameList(Map<String, Stream<String>> map) {
        return map.values().stream()
                .flatMap(i -> i)
                .filter(s -> s != null && !s.isBlank())
                .map(item -> item.replaceAll("\\s+", ""))
                .map(item -> item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase())
                .distinct()
                .sorted();
    }
}

