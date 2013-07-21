package net.avh4.data.per.features;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

public class FarmAnimal implements Serializable {
    private final @NotNull String name;

    private final int age;

    public FarmAnimal(@NotNull String name, int age) {
        checkNotNull(name);
        this.name = name;
        this.age = age;
    }

    @NotNull public String getName() {
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
