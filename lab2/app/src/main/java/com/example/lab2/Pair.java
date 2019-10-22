package com.example.lab2;

class Pair{
    public Pair(String value, char specifier)
    {
        this.value = value;
        this.specifier = specifier;
    }
    private String value;
    private char specifier;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public char getSpecifier() {
        return specifier;
    }

    public void setSpecifier(char specifier) {
        this.specifier = specifier;
    }
}
