package Kursach.shared.objects;

import java.io.Serializable;

public class Provider implements Serializable {
    private int id;
    private String name;
    private String email;

    public Provider(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Provider(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
