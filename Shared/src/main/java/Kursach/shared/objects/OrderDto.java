package Kursach.shared.objects;

import java.time.LocalDateTime;

public class OrderDto {
    private int id;
    private String product;
    private String client;

    private LocalDateTime dateTime;

    private int amount;

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

    public OrderDto(int id, String product, String client, LocalDateTime dateTime, int amount) {
        this.id = id;
        this.product = product;
        this.client = client;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }



    public OrderDto() {
    }
}
