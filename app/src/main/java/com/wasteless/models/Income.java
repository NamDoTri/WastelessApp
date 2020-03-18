package com.wasteless.models;

public class Income extends Transaction {
    private IncomeType type;
    private String source;

    public IncomeType getType() {
        return type;
    }

    public void setType(IncomeType type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
