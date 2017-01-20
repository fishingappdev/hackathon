package com.example.dmiadmin.hackathonapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dmiadmin.hackathonapp.model.Device;
import com.example.dmiadmin.hackathonapp.model.Employee;

/**
 * Created by pooja on 1/20/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "devicemanagement";

    // Device table name
    private static final String TABLE_DEVICE = "deviceinfo";

    // Device Table Columns names
    private static final String DEVICE_ID = "device_id";
    private static final String DEVICE_MANUFACTURER = "device_manufacturer_name";
    private static final String DEVICE_MODEL = "device_model";
    private static final String DEVICE_OS = "device_os";
    private static final String DEVICE_API = "device_api";

    //Employee table name
    private static final String TABLE_EMPLOYEE = "employeeinfo";

    // Employee Table Columns names
    private static final String EMPLOYEE_ID = "employee_id";
    private static final String EMPLOYEE_NAME = "employee_name";

    //Allocation table name
    private static final String TABLE_ALLOCATION = "allocationinfo";
    private static final String ID = "id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DEVICE + "("
                + DEVICE_ID + " TEXT PRIMARY KEY," + DEVICE_MANUFACTURER + " TEXT,"
                + DEVICE_MODEL + " TEXT," + DEVICE_OS + " TEXT," + DEVICE_API + " TEXT"+ ")";


        String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
                + EMPLOYEE_ID + " TEXT PRIMARY KEY," + EMPLOYEE_NAME + " TEXT"+ ")";

        String CREATE_TABLE_ALLOCATION = "CREATE TABLE " + TABLE_ALLOCATION + "("+ID +" INTEGER PRIMARY KEY   AUTOINCREMENT,"
                + EMPLOYEE_ID + " TEXT ," + DEVICE_ID  + " TEXT"+  ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_EMPLOYEE_TABLE);
        db.execSQL(CREATE_TABLE_ALLOCATION);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALLOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);

        // Create tables again
        onCreate(db);
    }

    // Adding new employee
   public void addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMPLOYEE_ID, employee.getEmployee_id()); // Employee id
        values.put(EMPLOYEE_NAME, employee.getEmployee_name()); // Employee name

        // Inserting Row
        db.insert(TABLE_EMPLOYEE, null, values);
       db.close();
    }

    public void addAllocation(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
         // Employee id
        values.put(EMPLOYEE_ID, device.getEmp_id()); // Employee name
        values.put(DEVICE_ID, device.getDevice_id());

        // Inserting Row
        db.insert(TABLE_ALLOCATION, null, values);
        db.close();
    }

    public int getContactsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_ALLOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        // return count
        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;

    }

    public String getEmployeeID(String deviceId) {

        Cursor cursor = null;
        String empName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        try{

            String query = "SELECT employee_id FROM allocationinfo WHERE device_id='" + deviceId+"'";
            cursor = db.rawQuery(query,null);
            if(cursor != null && !cursor.isClosed()) {
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    empName = cursor.getString(cursor.getColumnIndex("employee_id"));
                }
            }
            return empName;
        }finally {

            cursor.close();
        }
    }

    public String getEmployeeDetail(String empId) {

        Cursor cursor = null;
        String empDetail = "";
        String employeeId="";
        String empName="";
        SQLiteDatabase db = this.getReadableDatabase();
        try{

            String query = "SELECT * FROM employeeinfo WHERE employee_id='" + empId+"'";
            cursor = db.rawQuery(query,null);
            if(cursor != null && !cursor.isClosed()) {
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    employeeId = cursor.getString(cursor.getColumnIndex("employee_id"));
                     empName = cursor.getString(cursor.getColumnIndex("employee_name"));
                }
            }
            return empName+"("+employeeId+")";
        }finally {

            cursor.close();
        }
    }
}