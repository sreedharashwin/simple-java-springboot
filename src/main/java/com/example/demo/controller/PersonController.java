package com.example.demo.controller;

import com.example.demo.model.Person;

import java.util.List;

import com.example.demo.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PersonController {

    private final PersonService personService;

    PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String getAllPeople(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        return "index";
    }

    @PostMapping("/addPerson")
    public String createPerson(@ModelAttribute("person") Person person) {
        personService.save(person);
        return "redirect:/"; // Redirect to prevent duplicate form submissions
    }

    @PostMapping("/deletePerson")
    public String deletePerson(@RequestParam("id") String id) {
        personService.deleteById(id);
        return "redirect:/";
    }
}
