package Kursach.shared.objects;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderDto implements Serializable {
    private int id;
    private Product product;
    private Client client;
    private LocalDateTime dateTime;
    private int amount;

    public OrderDto() {}

    public OrderDto(int id, Product product, Client client, LocalDateTime dateTime, int amount) {
        this.id = id;
        this.product = product;
        this.client = client;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct (Product product){
        this.product = product;
    }

    public int getProductId() {
        return product.getId();
    }

    public void setProductId (int id){
        product.setId(id);
    }

    public String getProductName() {
        return product.getName();
    }

    public void setProductName (String name){
        product.setName(name);
    }

    public Client getClient() {
        return client;
    }

    public void setClient (Client client){
        this.client = client;
    }

    public int getClientId() {
        return client.getId();
    }

    public void setClientId (int id){
        client.setId(id);
    }

    public String getClientName() {
        return client.getName();
    }

    public void setClientName (String name){
        client.setName(name);
    }

    @Override
    public String toString() {
        return getProductName();
    }
}
