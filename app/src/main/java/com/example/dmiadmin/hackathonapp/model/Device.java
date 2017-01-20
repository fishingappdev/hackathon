package com.example.dmiadmin.hackathonapp.model;

/**
 * Created by pooja on 1/20/2017.
 */
public class Device {
    private String device_id;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    private String emp_id;

    public Device(String device_id, String emp_id) {
        this.device_id = device_id;
        this.emp_id = emp_id;
    }
}
