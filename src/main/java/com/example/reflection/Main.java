package com.example.reflection;

public class Main
{
    public static void main(String[] args)
    {
        Injector injector = new Injector();
        SomeBean sb = injector.inject(new SomeBean());
        sb.foo();
    }
}