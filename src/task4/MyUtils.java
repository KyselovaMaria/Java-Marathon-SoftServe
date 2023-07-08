package task4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class MyUtils {
    public List<Employee> largestEmployees(List<Employee> workers) {
        if (workers.isEmpty() || workers.get(0) == null || workers.size() == 1) return workers;

        BigDecimal employeeMax = workers.stream()
                .filter(Predicate.not(Manager.class::isInstance))
                .max(Comparator.comparing(Employee::getPayment))
                .get().getPayment();

        BigDecimal managerMax = workers.stream()
                .filter(Manager.class::isInstance)
                .max(Comparator.comparing(Employee::getPayment))
                .get().getPayment();

        int maxEmployeeExperience = workers.stream()
                .filter(Predicate.not(Manager.class::isInstance))
                .max(Comparator.comparing(Employee::getExperience))
                .get().getExperience();

        int maxManagerExperience = workers.stream()
                .filter(Manager.class::isInstance)
                .max(Comparator.comparing(Employee::getExperience))
                .get().getExperience();

        List<Employee> resultList = new ArrayList<>();

        workers.forEach(worker -> {
            if (worker instanceof Manager){
                if (worker.getExperience() == maxManagerExperience || worker.getPayment().equals(managerMax)){
                    resultList.add(worker);
                }
            } else {
                if (worker.getExperience() == maxEmployeeExperience || worker.getPayment().equals(employeeMax)){
                    resultList.add(worker);
                }
            }
        });

        return resultList;
    }

}