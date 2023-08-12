package com.softserve.edu.sprint4.task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

public class MyUtils {
    public Map<String, List<String>> createNotebook(Map<String, String> phones) {
        Map<String, List<String>> map = new HashMap<>();

        for (String phone : phones.keySet()) {
            String name = phones.get(phone);

            if (map.containsKey(name)) {
                map.get(name)           // List<String>
                        .add(phone);    // add to list
            } else {
                map.put(name, new ArrayList<>(){{add(phone);}});
            }

        }

        return map;
    }
}
