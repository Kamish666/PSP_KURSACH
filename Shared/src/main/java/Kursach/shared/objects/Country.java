package Kursach.shared.objects;

import java.io.Serializable;

public class Country implements Serializable {
    private int id;
    private String country;

    public Country(int id, String country){
        this.id=id;
        this.country=country;
    }
    public Country() {}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  country;
    }
}
