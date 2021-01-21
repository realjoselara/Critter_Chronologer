package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.CustomersRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeesRepository;
import com.udacity.jdnd.course3.critter.repositories.PetsRepository;
import com.udacity.jdnd.course3.critter.repositories.SchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulesService {

    private SchedulesRepository schedulesRepository;
    private PetsRepository petsRepository;
    private EmployeesRepository employeesRepository;
    private CustomersRepository customersRepository;

    @Autowired
    public SchedulesService(SchedulesRepository schedulesRepository, PetsRepository petsRepository, EmployeesRepository employeesRepository, CustomersRepository customersRepository){
        this.customersRepository = customersRepository;
        this.petsRepository = petsRepository;
        this.employeesRepository = employeesRepository;
        this.schedulesRepository = schedulesRepository;
    }

    public List<Schedule> getAllSchedules() {
        return schedulesRepository.findAll();
    }

    public List<Schedule> getAllSchedulesForPet(long petId) {
        Pet pet = petsRepository.getOne(petId);
        return schedulesRepository.getAllByPetsContains(pet);
    }

    public List<Schedule> getAllSchedulesForEmployee(long employeeId) {
        Employee employee = employeesRepository.getOne(employeeId);
        return schedulesRepository.getAllByEmployeesContains(employee);
    }

    public List<Schedule> getAllSchedulesForCustomer(long customerId) {
        Customer customer = customersRepository.getOne(customerId);
        return  schedulesRepository.getAllByPetsIn(customer.getPets());
    }

    public Schedule saveSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Employee> employees = employeesRepository.findAllById(employeeIds);
        List<Pet> pets = petsRepository.findAllById(petIds);
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return schedulesRepository.save(schedule);
    }
}