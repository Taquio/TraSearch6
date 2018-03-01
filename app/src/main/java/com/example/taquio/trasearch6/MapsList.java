package com.example.taquio.trasearch6;

/**
 * Created by ARVN on 2/28/2018.
 */

public class MapsList
{
    public String name;
    public String address;
    public String phone;

    public MapsList(String name, String address, String phone) {

        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
