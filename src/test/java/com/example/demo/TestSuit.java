package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.demo.model.Person;

public class TestSuit {

    private static String message;
    private static LinkedList<Person> list = new LinkedList<>();

    @BeforeAll
    public static void init()  {
         message = "This is the intended message";
         Person p = new Person("John Doe", 20);
         Person p1 = new Person("Jane Doe",20);
         list.add(p);
         list.add(p1);
    }

    @Test 
    public void testMessage() {
        System.out. println("Inside testMessage()");
        assertEquals(message, "This is the intended message");
    }

    @Test
    public void testList(){
        System.out.println("Running test to check if persons are fetched: ");
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getName(), "John Doe");
    }
}
