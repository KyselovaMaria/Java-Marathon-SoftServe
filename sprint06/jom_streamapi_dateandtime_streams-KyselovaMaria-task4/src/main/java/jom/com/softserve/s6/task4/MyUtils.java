package jom.com.softserve.s6.task4;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;

public class MyUtils {

    public static String readFile(String filename) {
        String input;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            input = reader.readLine();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();
        for (String ch: input.split("(?<=\\G.{7})")){
            sb.append((char)Integer.parseInt(ch, 2));
        }

        return sb.toString();

    }

}
