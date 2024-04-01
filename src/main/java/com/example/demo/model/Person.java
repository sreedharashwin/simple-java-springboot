package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "person")
public class Person {
    @Id
    private String id;
    private String name;
    private int age;
    public String getId() {
        return id;
    }
    public Person() {
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Constructors, getters, and setters
}
