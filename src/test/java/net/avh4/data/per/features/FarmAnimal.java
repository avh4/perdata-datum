package net.avh4.data.per.features;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

public class FarmAnimal implements Serializable {
    private final String name;

    private final int age;

    public FarmAnimal(String name, int age) {
        checkNotNull(name);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override public String toString() {
        return "FarmAnimal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FarmAnimal that = (FarmAnimal) o;

        if (age != that.age) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        return result;
    }
}
