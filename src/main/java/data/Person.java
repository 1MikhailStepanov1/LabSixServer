package data;

public class Person {
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private Integer weight; //Поле может быть null, Значение поля должно быть больше 0

    public Person(Long height, Integer weight) {
        this.height = height;
        this.weight = weight;
    }

    public Long getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }
}
