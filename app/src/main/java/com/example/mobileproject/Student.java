package com.example.mobileproject;

public class Student {
    private String name,gender, nationalID,fatherName,surName,DOB,ID;

    public Student(){

    }

    public Student(String ID, String name, String fatherName, String surName, String nationalID, String DOB,String gender){
        this.ID=ID;
        this.name=name;
        this.fatherName=fatherName;
        this.surName=surName;
        this.nationalID=nationalID;
        this.DOB=DOB;
        this.gender=gender;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
