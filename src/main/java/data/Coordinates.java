package data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"coordinateX", "coordinateY"})
public class Coordinates {
    private long x; //Максимальное значение поля: 768
    private Integer y; //Поле не может быть null

    public Coordinates(long x, Integer y) {
        this.x = x;
        this.y = y;
    }
    @XmlElement(name = "coordinateX")
    public void setCoordinateX(long x){
        this.x = x;
    }
    @XmlElement(name = "coordinateY")
    public void setCoordinateY(Integer y){
        this.y = y;
    }
    public long getCoordinateX() {
        return x;
    }

    public Integer getCoordinateY() {
        return y;
    }

}
