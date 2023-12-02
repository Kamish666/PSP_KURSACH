package Kursach.shared.objects;

import java.io.Serializable;

public class ProductCategory implements Serializable {
        private int id;
        private String category;
        private String definition;

        public ProductCategory(int id, String category, String definition){
            this.id = id;
            this.category = category;
            this.definition = definition;
        }

        public ProductCategory(){}

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    @Override
    public String toString() {
        return getCategory();
    }
}