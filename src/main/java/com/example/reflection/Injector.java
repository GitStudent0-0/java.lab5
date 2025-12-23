package com.example.reflection;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
/**
 * Читает настройки из config.properties и
 * подставляет реализации в поля с @AutoInjectable.
 */
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
    /**
     * Находит помеченные поля и создаёт для них объекты
     * указанных в config.properties классов.
     *
     * @param object объект, в который нужно внедрить зависимости
     * @param <T> тип объекта
     * @return тот же объект с заполненными полями
     */
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