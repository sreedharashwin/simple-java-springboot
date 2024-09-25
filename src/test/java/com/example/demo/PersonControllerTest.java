package com.example.demo;

import com.example.demo.controller.PersonController;
import com.example.demo.model.Person;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonControllerTest {

    @Mock
    private PersonService personService;

    @Mock
    Model model;

    @InjectMocks
    PersonController personController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPersons(){
        List<Person> people = Arrays.asList(new Person("John", 24), new Person("Jane", 25)
        , new Person("Jack", 26));
        when(personService.findAll()).thenReturn(people);
        assertEquals("index",personController.getAllPeople(model));
    }

    @Test
    void testCreatePerson(){
        Person person = new Person("John", 24);
        assertEquals("redirect:/",personController.createPerson(person));
        verify(personService).save(person);
    }

    @Test
    void deletePerson(){
        String id = "1234";
        assertEquals("redirect:/",personController.deletePerson(id));
        verify(personService).deleteById(id);
    }
}
