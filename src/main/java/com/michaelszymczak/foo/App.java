package com.michaelszymczak.foo;

public class App {
    public String getGreeting() {
        return "Hello Foo.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
