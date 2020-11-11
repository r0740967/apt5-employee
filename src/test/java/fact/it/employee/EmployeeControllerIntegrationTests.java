package fact.it.employee;

import fact.it.employee.model.Employee;
import fact.it.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void beforeAllTests() {
        employeeRepository.deleteAll();
        employeeRepository.save(new Employee("E01", "H01", "Emp", "Loyee", "Barman"));
        employeeRepository.save(new Employee("E02", "H02", "Emp", "Loyee", "Kuisvrouw"));
        employeeRepository.save(new Employee("E03", "H02", "Emp", "Loyee", "Manager"));
    }

    @AfterEach
    public void afterAllTests() {
        employeeRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenEmployee_whenGetAllEmployees_thenReturnJsonEmployees() throws Exception {
        mockMvc.perform(get("/employees/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].employeeCode", is("E01")))
                .andExpect(jsonPath("$.[0].hotelCode", is("H01")))
                .andExpect(jsonPath("$.[0].firstName", is("Emp")))
                .andExpect(jsonPath("$.[0].lastName", is("Loyee")))
                .andExpect(jsonPath("$.[0].function", is("Barman")))
                .andExpect(jsonPath("$.[1].employeeCode", is("E02")))
                .andExpect(jsonPath("$.[1].hotelCode", is("H02")))
                .andExpect(jsonPath("$.[1].firstName", is("Emp")))
                .andExpect(jsonPath("$.[1].lastName", is("Loyee")))
                .andExpect(jsonPath("$.[1].function", is("Kuisvrouw")))
                .andExpect(jsonPath("$.[2].employeeCode", is("E03")))
                .andExpect(jsonPath("$.[2].hotelCode", is("H02")))
                .andExpect(jsonPath("$.[2].firstName", is("Emp")))
                .andExpect(jsonPath("$.[2].lastName", is("Loyee")))
                .andExpect(jsonPath("$.[2].function", is("Manager")));
    }

    @Test
    public void givenEmployee_whenGetEmployeesByCode_thenReturnJsonEmployees() throws Exception {
        mockMvc.perform(get("/employees/{employeeCode}/{hotelCode}", "E01", "H01"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode", is("E01")))
                .andExpect(jsonPath("$.hotelCode", is("H01")))
                .andExpect(jsonPath("$.firstName", is("Emp")))
                .andExpect(jsonPath("$.lastName", is("Loyee")))
                .andExpect(jsonPath("$.function", is("Barman")));
    }

    @Test
    public void givenEmployee_whenGetEmployeesByHotelCode_thenReturnJsonEmployees() throws Exception {
        mockMvc.perform(get("/employees/{hotelCode}", "H02"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].employeeCode", is("E02")))
                .andExpect(jsonPath("$.[0].hotelCode", is("H02")))
                .andExpect(jsonPath("$.[0].firstName", is("Emp")))
                .andExpect(jsonPath("$.[0].lastName", is("Loyee")))
                .andExpect(jsonPath("$.[0].function", is("Kuisvrouw")))
                .andExpect(jsonPath("$.[1].employeeCode", is("E03")))
                .andExpect(jsonPath("$.[1].hotelCode", is("H02")))
                .andExpect(jsonPath("$.[1].firstName", is("Emp")))
                .andExpect(jsonPath("$.[1].lastName", is("Loyee")))
                .andExpect(jsonPath("$.[1].function", is("Manager")));;
    }

    @Test
    public void whenPostEmployee_thenReturnJsonEmployee() throws Exception {
        Employee employeeE04 = new Employee("E04", "H03", "Emp", "Loyee", "Kok");

        mockMvc.perform(post("/employees")
                .content(mapper.writeValueAsString(employeeE04))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode", is("E04")))
                .andExpect(jsonPath("$.hotelCode", is("H03")))
                .andExpect(jsonPath("$.firstName", is("Emp")))
                .andExpect(jsonPath("$.lastName", is("Loyee")))
                .andExpect(jsonPath("$.function", is("Kok")));
    }

    @Test
    public void givenEmployee_whenPutEmployee_thenReturnJsonReview() throws Exception {
        Employee updatedEmployee = new Employee("E01", "H01", "Emp", "Loyee", "SousChef");

        mockMvc.perform(put("/employees")
                .content(mapper.writeValueAsString(updatedEmployee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode", is("E01")))
                .andExpect(jsonPath("$.hotelCode", is("H01")))
                .andExpect(jsonPath("$.firstName", is("Emp")))
                .andExpect(jsonPath("$.lastName", is("Loyee")))
                .andExpect(jsonPath("$.function", is("SousChef")));
    }

    @Test
    public void givenEmployee_whenDeleteEmployee_thenStatusOk() throws Exception {
        mockMvc.perform(delete("/employees/{employeeCode}/{hotelCode}", "E01", "H01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenEmployee_whenDeleteEmployee_thenStatusNotfound() throws Exception {
        mockMvc.perform(delete("/employees/{employeeCode}/{hotelCode}", "E05", "H01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
