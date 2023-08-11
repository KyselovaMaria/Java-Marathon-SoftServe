package task3;

import java.util.List;
import java.util.stream.Collectors;


public class MyUtils {
    public List<Person> maxDuration(List<Person> people) {
        int studentMax = people.stream()
                .filter(Student.class::isInstance)//Student.class::cast
                .mapToInt(Person::getYears)
                .max().orElse(-1);

        int workerMax = people.stream()
                .filter(Worker.class::isInstance)
                .mapToInt(Person::getYears)
                .max().orElse(-1);

        return people.stream()
                .filter(person -> (person instanceof Student && person.getYears() >= studentMax)
                        || (person instanceof Worker && person.getYears() >= workerMax))
                .collect(Collectors.toList());
    }
}