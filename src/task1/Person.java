package task1;

class Person {
    int age;
    String healthInfo;
    String name;

    public Person(int age, String healthInfo, String name) {
        this.age = age;
        this.healthInfo = healthInfo;
        this.name = name;
    }

    String getHealthStatus() {
        return name + " " + healthInfo;
    }
}
