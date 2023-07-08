enum Color {
    WHITE, RED, BLUE;
}

enum Type {
    RARE, ORDINARY;
}

class ColorException extends Exception {
    public ColorException(String message) {
        super(message);
    }
}

class TypeException extends Exception {
    public TypeException(String message) {
        super(message);
    }
}

public class Plant {
    private String name;
    private Color color;
    private Type type;

    public void setColor(String color) throws ColorException {
        try {
            this.color = Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ColorException("Invalid color: " + color);
        }
    }

    public void setType(String type) throws TypeException {
        try {
            this.type = Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TypeException("Invalid type: " + type);
        }
    }

    public Plant(String type, String color, String name) throws TypeException, ColorException {
        try {
            setType(type);
        } catch (TypeException e) {
            throw new TypeException("Invalid value " + type + " for field type");
        }

        try {
            setColor(color);
        } catch (ColorException e) {
            throw new ColorException("Invalid value " + color + " for field color");
        }

        this.name = name;
    }

    //task6
    public static Plant tryCreatePlant(String type, String color, String name) throws TypeException, ColorException {
        try {
            return new Plant(type, color, name);
        } catch (TypeException e) {
            try {
                return new Plant("Ordinary", color, name);
            } catch (ColorException ex) {
                return new Plant("Ordinary", "Red", name);
                //throw new ColorException("Invalid color: " + color);
            }
        } catch (ColorException e) {
            try {
                return new Plant(type, "Red", name);
            } catch (TypeException ex) {
                return new Plant("Ordinary", "Red", name);
                //throw new TypeException("Invalid type: " + type);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{type: " + type + ", color: " + color + ", name: " + name + "}";
    }
}



