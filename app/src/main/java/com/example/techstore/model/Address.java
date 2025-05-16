package com.example.techstore.model;

public class Address {
    private String type;
    private String name;
    private String phone;
    private String detail;
    private String province;
    private String district;
    private String ward;

    public Address(String detail, String district, String name, String phone, String province, String type, String ward) {
        this.detail = detail;
        this.district = district;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.type = type;
        this.ward = ward;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
