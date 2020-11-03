package fact.it.employee.controller;

import fact.it.employee.model.Employee;
import fact.it.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void FillDatabase(){
        employeeRepository.deleteAll();
        if (employeeRepository.count() == 0){
            employeeRepository.save(new Employee("E01", "H01", "Emp", "Loyee", "Barman"));
            employeeRepository.save(new Employee("E02", "H02", "Emp", "Loyee", "Kuisvrouw"));
            employeeRepository.save(new Employee("E03", "H03", "Emp", "Loyee", "Manager"));
        }
        System.out.println("Aantal werknemers: " + employeeRepository.count());
    }

    @GetMapping("/employees/all")
    public List<Employee> getAllItems(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{code}")
    public Employee getByCode(@PathVariable String code){
        return employeeRepository.findEmployeeByCode(code);
    }

    @PostMapping("/employees")
    public void addEmployee(@RequestBody Employee newEmployee){
        employeeRepository.save(newEmployee);
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employeeToUpdate){
        Employee retrievedEmployee = employeeRepository.findEmployeeByCode(employeeToUpdate.getCode());
        retrievedEmployee.setCode(employeeToUpdate.getCode());
        retrievedEmployee.setFirstName(employeeToUpdate.getFirstName());
        retrievedEmployee.setLastName(employeeToUpdate.getLastName());
        retrievedEmployee.setHotelCode(employeeToUpdate.getHotelCode());
        retrievedEmployee.setFunction(employeeToUpdate.getFunction());

        employeeRepository.save(retrievedEmployee);
        return retrievedEmployee;
    }

    @DeleteMapping("/employees/{code}")
    public ResponseEntity deleteEmployee(@PathVariable String code){
        Employee employee = employeeRepository.findEmployeeByCode(code);
        if (employee != null){
            employeeRepository.delete(employee);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
