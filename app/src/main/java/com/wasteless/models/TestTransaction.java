package com.wasteless.models;


//THIS IS JUST A TEMPORARY MODEL TO MAKE IT EASIER TO TEST OUT THE RECYCLERVIEW & ITS ADAPTER

public class TestTransaction {

    public String description;
    public String category;
    public String amount;

    public TestTransaction(String description, String category, String amount) {
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
