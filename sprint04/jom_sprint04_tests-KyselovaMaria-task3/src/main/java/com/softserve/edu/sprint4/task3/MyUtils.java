package com.softserve.edu.sprint4.task3;

import java.util.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;



public class MyUtils {
    public boolean listMapCompare(List<String> list, Map<String, String> map) {
        Set<String> listValues = new HashSet<>(list);

        Set<String> mapValues = new HashSet<>();
        for (String key : map.keySet()) {
            mapValues.add(map.get(key));
        }

        return listValues.containsAll(mapValues) && mapValues.containsAll(listValues);

    }

    public boolean listMapCompare2(List<String> list, Map<String, String> map) {
        Set<String> mapValues = new HashSet<>();
        for (String key : map.keySet()) {
            mapValues.add(map.get(key));
        }

        return list.containsAll(mapValues) && mapValues.containsAll(list);
    }
}
