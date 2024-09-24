package com.example.demo.service;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonService implements IPersonService{

    private final PersonRepository personRepository;

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
         personRepository.deleteById(id);
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

}
