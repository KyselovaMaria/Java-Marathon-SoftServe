package jom.com.softserve.s3.task4;

//Describe LineType enum here

public class Line {
    public enum LineType {
        SOLID, DOTTED, DASHED, DOUBLE
    }

    public static String drawLine(LineType lineType) {
        String type = lineType.toString().toLowerCase();
        return "The line is " + type + " type";
    }

}
