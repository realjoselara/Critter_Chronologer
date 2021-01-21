package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.services.CustomersService;
import com.udacity.jdnd.course3.critter.services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private CustomersService customersService;
    private EmployeesService employeesService;


    @Autowired
    public UserController(EmployeesService employeesService, CustomersService
                          customersService){
        this.customersService = customersService;
        this.employeesService = employeesService;
    }

    private CustomerDTO getCustomerDTO(Customer saveCustomer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(saveCustomer.getId());
        customerDTO.setName(saveCustomer.getName());
        customerDTO.setPhoneNumber(saveCustomer.getPhoneNumber());
        customerDTO.setNotes(saveCustomer.getNotes());
        List<Long> petIds = saveCustomer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private EmployeeDTO getEmployeeDTO(Employee saveEmployee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(saveEmployee.getId());
        employeeDTO.setName(saveEmployee.getName());
        employeeDTO.setSkills(saveEmployee.getSkills());
        employeeDTO.setDaysAvailable(saveEmployee.getDaysAvailable());
        return employeeDTO;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        List<Long> petIds = customerDTO.getPetIds();
        return getCustomerDTO(customersService.saveCustomer(customer, petIds));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customersService.getAllCustomers();
        return customers.stream().map(this::getCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return getCustomerDTO(customersService.getCustomerByPetId(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        return getEmployeeDTO(employeesService.saveEmployee(employee));
    }


    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return getEmployeeDTO(employeesService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeesService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeesService.getEmployeesForService(employeeDTO.getDate(), employeeDTO.getSkills());
        return employees.stream().map(this::getEmployeeDTO).collect(Collectors.toList());
    }

}
