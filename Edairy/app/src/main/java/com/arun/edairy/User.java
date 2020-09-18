package com.arun.edairy;

class User {
    private String name;
    private String phone;
    private byte[] pictureUrl;

    public User() {
    }

    public User(String name, String phone, byte[] pictureUrl) {
        this.name = name;
        this.phone = phone;
        this.pictureUrl = pictureUrl;
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

    public byte[] getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(byte[] pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
