package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomersRepository;
import com.udacity.jdnd.course3.critter.repositories.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsService {

    private PetsRepository petsRepository;
    private CustomersRepository customersRepository;

    @Autowired
    public PetsService(CustomersRepository customersRepository, PetsRepository petsRepository){
        this.petsRepository = petsRepository;
        this.customersRepository = customersRepository;
    }


    public List<Pet> getAllPets() {
        return petsRepository.findAll();
    }

    public List<Pet> getPetsByCustomerId(long customerId) {
        return petsRepository.getAllByCustomerId(customerId);
    }

    public Pet getPetById(long petId) {
        return petsRepository.getOne(petId);
    }

    public Pet savePet(Pet pet, long ownerId) {
        Customer customer = customersRepository.getOne(ownerId);
        pet.setCustomer(customer);
        pet = petsRepository.save(pet);
        customer.insertPet(pet);
        customersRepository.save(customer);
        return pet;
    }
}