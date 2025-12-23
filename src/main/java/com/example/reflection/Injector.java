package com.example.reflection;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class Injector
{

    private final Properties properties;

    public Injector()
    {
        properties = new Properties();
        try (InputStream input = Injector.class
                .getClassLoader()
                .getResourceAsStream("config.properties"))
        {
            if (input == null)
            {
                System.out.println("Не удалось найти config.properties");
                return;
            }
            properties.load(input);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public <T> T inject(T object)
    {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(AutoInjectable.class))
            {
                String interfaceName = field.getType().getName();
                String implClassName = properties.getProperty(interfaceName);

                if (implClassName != null)
                {
                    try
                    {
                        Class<?> implClass = Class.forName(implClassName);
                        Object implInstance = implClass
                                .getDeclaredConstructor()
                                .newInstance();

                        field.setAccessible(true);
                        field.set(object, implInstance);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                    System.out.println("Реализация не найдена для: " + interfaceName);

            }
        }
        return object;
    }
}