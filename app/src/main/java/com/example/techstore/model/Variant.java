package com.example.techstore.model;

import java.util.List;

public class Variant {
    private String type;
    private String size;
    private List<String> listType;
    private List<String> listSize;

    public Variant(List<String> listSize, List<String> listType, String size, String type) {
        this.listSize = listSize;
        this.listType = listType;
        this.size = size;
        this.type = type;
    }

    public List<String> getListSize() {
        return listSize;
    }

    public void setListSize(List<String> listSize) {
        this.listSize = listSize;
    }

    public List<String> getListType() {
        return listType;
    }

    public void setListType(List<String> listType) {
        this.listType = listType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
