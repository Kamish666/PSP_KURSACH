package Kursach.shared.objects;

import java.io.Serializable;

public class Manufacturer implements Serializable {
    private int id;
    private String name;
    private int countryId;

    public Manufacturer(int id, String name, int countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    public Manufacturer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
