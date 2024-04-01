package com.example.demo.repository;

import com.example.demo.model.Person;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

    List<Person> findByNameContainingIgnoreCase(String keyword);
}
