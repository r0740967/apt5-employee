package fact.it.employee;
import fact.it.employee.model.Employee;
import fact.it.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenEmployee_whenGetAllEmployees_thenReturnJsonEmployees() throws Exception {
        Employee employeeE01 = new Employee("E01", "H01", "Emp", "Loyee", "Barman");
        Employee employeeE02 = new Employee("E02", "H02", "Emp", "Loyee", "Kuisvrouw");
        Employee employeeE03 = new Employee("E03", "H02", "Emp", "Loyee", "Manager");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employeeE01);
        employeeList.add(employeeE02);
        employeeList.add(employeeE03);

        given(employeeRepository.findAll()).willReturn(employeeList);

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
        Employee employeeE01 = new Employee("E01", "H01", "Emp", "Loyee", "Barman");

        given(employeeRepository.findEmployeeByEmployeeCodeAndHotelCode("E01","H01")).willReturn(employeeE01);

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
        Employee employeeE02 = new Employee("E02", "H02", "Emp", "Loyee", "Kuisvrouw");
        Employee employeeE03 = new Employee("E03", "H02", "Emp", "Loyee", "Kok");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employeeE02);
        employeeList.add(employeeE03);

        given(employeeRepository.findEmployeeByHotelCode("H02")).willReturn(employeeList);

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
                .andExpect(jsonPath("$.[1].function", is("Kok")));;
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
        Employee employeeE04 = new Employee("E04", "H03", "Emp", "Loyee", "Kok");

        given(employeeRepository.findEmployeeByEmployeeCodeAndHotelCode("E04","H03")).willReturn(employeeE04);

        Employee updatedEmployee = new Employee("E04", "H03", "Emp", "Loyee", "SousChef");

        mockMvc.perform(put("/employees")
                .content(mapper.writeValueAsString(updatedEmployee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode", is("E04")))
                .andExpect(jsonPath("$.hotelCode", is("H03")))
                .andExpect(jsonPath("$.firstName", is("Emp")))
                .andExpect(jsonPath("$.lastName", is("Loyee")))
                .andExpect(jsonPath("$.function", is("SousChef")));
    }

    @Test
    public void givenEmployee_whenDeleteEmployee_thenStatusOk() throws Exception {
        Employee employeeE04 = new Employee("E04", "H03", "Emp", "Loyee", "Kok");

        given(employeeRepository.findEmployeeByEmployeeCodeAndHotelCode("E04","H03")).willReturn(employeeE04);

        mockMvc.perform(delete("/employees/{employeeCode}/{hotelCode}", "E04", "H03")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenEmployee_whenDeleteEmployee_thenStatusNotfound() throws Exception {
        given(employeeRepository.findEmployeeByEmployeeCodeAndHotelCode("E05","H03")).willReturn(null);

        mockMvc.perform(delete("/employees/{employeeCode}/{hotelCode}", "E05", "H01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

