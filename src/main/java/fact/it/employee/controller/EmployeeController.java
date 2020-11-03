package fact.it.employee.controller;

import fact.it.employee.model.Employee;
import fact.it.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void FillDatabase(){
        if (employeeRepository.count() == 0){
            employeeRepository.save(new Employee("E01", "H01", "Emp", "Loyee", "Barman"));
        }
        System.out.println("Aantal werknemers" + employeeRepository.count());
    }
}
