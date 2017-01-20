package com.example.dmiadmin.hackathonapp.model;

/**
 * Created by pooja on 1/20/2017.
 */
public class Employee {
    private String employee_id;

    public Employee(String emp_id, String emp_name) {
        this.employee_id=emp_id;
        this.employee_name=emp_name;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    private String employee_name;
}
