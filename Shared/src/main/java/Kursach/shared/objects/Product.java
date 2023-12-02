package Kursach.shared.objects;

import java.io.Serializable;

public class Product implements Serializable {
        private int id;
        private String name;
        private double price;
        private int categoryId;
        private int manufacturerId;
        private int providerId;

        public Product(int id, String name, double price, int categoryId, int manufacturerId, int providerId) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.categoryId = categoryId;
            this.manufacturerId = manufacturerId;
            this.providerId = providerId;
        }

        public Product() {
        }

    public Product(ProductDto productDto) {
        this.id = productDto.getId();
        this.name = productDto.getName();
        this.price = productDto.getPrice();
        this.categoryId = productDto.getCategoryId();
        this.manufacturerId = productDto.getManufacturerId();
        this.providerId = productDto.getProviderId();
    }

    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getManufacturerId() {
            return manufacturerId;
        }

        public void setManufacturerId(int manufacturerId) {
            this.manufacturerId = manufacturerId;
        }

        public int getProviderId() {
            return providerId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.providerId = providerId;
        }

    @Override
    public String toString() {
        return getName();
    }
}