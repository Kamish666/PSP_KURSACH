package Kursach.shared.objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order implements Serializable {
    private int id;
    private int productId;
    private int clientId;
    LocalDateTime dateTime;
    private int amount;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static String dateToString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public static LocalDateTime stringToLocaDateTime(String string) {
        return LocalDateTime.parse(string, formatter);
    }




    public Order() {
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

    public Order(int id, int productId, int clientId, LocalDateTime dateTime, int ammount) {
        this.id = id;
        this.productId = productId;
        this.clientId = clientId;
        this.dateTime = dateTime;
        this.amount = ammount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}