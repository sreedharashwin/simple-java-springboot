package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ComponentScan
@Controller
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/people")
    public String getAllPeople(Model model) {
        List<Person> people = personRepository.findAll();
        model.addAttribute("people", people);
        return "index";
    }

    @PostMapping("/addPerson")
    public String createPerson(@ModelAttribute("person") Person person) {
        personRepository.save(person);
        return "redirect:/people"; // Redirect to prevent duplicate form submissions
    }

    // Handle the display all users action
    @GetMapping("/allUsers")
    public String displayAllUsers(Model model) {
        model.addAttribute("people", personRepository.findAll());
        model.addAttribute("person", new Person()); // For the form
        return "index"; // Return to the same page with all users displayed
    }
}
