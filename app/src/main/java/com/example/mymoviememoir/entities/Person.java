package com.example.mymoviememoir.entities;

import java.sql.Date;

public class Person {
    private int personId;
    private String personFname;
    private String personSurname;
    private String personDob;
    private String personGender;
    private String personAddress;
    private String personState;
    private int personPostcode;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonFname() {
        return personFname;
    }

    public void setPersonFname(String personFname) {
        this.personFname = personFname;
    }

    public String getPersonSurname() {
        return personSurname;
    }

    public void setPersonSurname(String personSurname) {
        this.personSurname = personSurname;
    }

    public String getPersonDob() {
        return personDob;
    }

    public void setPersonDob(String personDob) {
        this.personDob = personDob;
    }

    public String getPersonGender() {
        return personGender;
    }

    public void setPersonGender(String personGender) {
        this.personGender = personGender;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getPersonState() {
        return personState;
    }

    public void setPersonState(String personState) {
        this.personState = personState;
    }

    public int getPersonPostcode() {
        return personPostcode;
    }

    public void setPersonPostcode(int personPostcode) {
        this.personPostcode = personPostcode;
    }

    public Person(int personId){
        this.personId = personId;
    }

    public Person(int personId, String personFname, String personSurname, String personDob, String personGender, String personAddress, String personState, int personPostcode){
        this.personId = personId;
        this.personFname = personFname;
        this.personSurname = personSurname;
        this.personDob = personDob;
        this.personGender = personGender;
        this.personAddress = personAddress;
        this.personState = personState;
        this.personPostcode = personPostcode;
    }

}
