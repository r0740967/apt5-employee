package fact.it.employee.repository;

import fact.it.employee.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository <Employee, String> {
    Employee findEmployeeByCode(String code);
}
