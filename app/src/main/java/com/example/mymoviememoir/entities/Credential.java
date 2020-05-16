package com.example.mymoviememoir.entities;

import java.sql.Date;

public class Credential {
    private int credId;
    private Person personId;
    private String credUsername;
    private String credHash;
    private String credSignupdate;

    public Credential(int credId, Person personId, String credUsername, String credHash, String credSignupdate)
    {
        this.credId = credId;
        this.credUsername = credUsername;
        this.credHash = credHash;
        this.credSignupdate = credSignupdate;
        this.personId = personId;
    }

    public int getCredId() {
        return credId;
    }

    public void setCredId(){
        this.credId = credId;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    public String getCredUsername() {
        return credUsername;
    }

    public void setCredUsername(String credUsername) {
        this.credUsername = credUsername;
    }

    public String getCredHash() {
        return credHash;
    }

    public void setCredHash(String credHash) {
        this.credHash = credHash;
    }

    public String getCredSignupdate() {
        return credSignupdate;
    }

    public void setCredSignupdate(String credSignupdate) {
        this.credSignupdate = credSignupdate;
    }
}
