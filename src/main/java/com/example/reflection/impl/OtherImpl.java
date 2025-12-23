package com.example.reflection.impl;

import com.example.reflection.interfaces.SomeInterface;

public class OtherImpl implements SomeInterface
{
    @Override
    public void doSomething()
    {
        System.out.print("B");
    }
}