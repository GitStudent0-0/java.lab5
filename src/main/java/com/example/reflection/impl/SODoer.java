package com.example.reflection.impl;

import com.example.reflection.interfaces.SomeOtherInterface;

public class SODoer implements SomeOtherInterface
{
    @Override
    public void doSomeOther()
    {
        System.out.print("C");
    }
}