package Kursach.shared.objects;

public class ManufacturerDto {
    private int id;
    private String name;
    private String country;

    public ManufacturerDto(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public ManufacturerDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountryId(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
