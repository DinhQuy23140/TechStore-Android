package com.example.techstore.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import java.util.Objects;

public class Address {
    private String type;
    private String name;
    private String phone;
    private String detail;
    private String province;
    private String district;
    private String ward;
    private boolean isDefault;

    public Address() {
    }

    public Address(String detail, String district, String name, String phone, String province, String type, String ward, boolean isDefault) {
        this.detail = detail;
        this.district = district;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.type = type;
        this.ward = ward;
        this.isDefault = isDefault;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @NonNull
    @Override
    public String toString() {
        return getWard() + ", " + getDistrict() + ", " + getProvince();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Address)) return false;
        Address address = (Address) obj;
        return Objects.equals(toString(), address.toString()) &&
                Objects.equals(getName(), address.getName()) &&
                Objects.equals(getPhone(), address.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString(), name, phone);
    }
}

