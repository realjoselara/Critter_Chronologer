package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomersRepository;
import com.udacity.jdnd.course3.critter.repositories.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomersService {

    private CustomersRepository customersRepository;
    private PetsRepository petsRepository;

    @Autowired
    public CustomersService(CustomersRepository customersRepository, PetsRepository petsRepository){
        this.petsRepository = petsRepository;
        this.customersRepository = customersRepository;
    }

    public List<Customer> getAllCustomers() {
        return customersRepository.findAll();
    }

    public Customer getCustomerByPetId(long petId) {
        return petsRepository.getOne(petId).getCustomer();
    }

    public Customer saveCustomer(Customer customer, List<Long> petIds) {
        List<Pet> pets = new ArrayList<>();
        if (petIds != null && !petIds.isEmpty()) {
            pets = petIds.stream().map((petId) -> petsRepository.getOne(petId)).collect(Collectors.toList());
        }
        customer.setPets(pets);
        return customersRepository.save(customer);
    }
}