package edu.ufp.inf.sd.rmi.project.server;

import java.io.Serializable;

/**
 * This class holds the informartion for any given user.
 *
 *
 *
 */
public class User implements Serializable {
    private String uname;
    private String pword;
    private int credits;

    /**
     * This constructor initiates the class attributes.
     */
    public User(String uname, String pword) {
        this.uname = uname;
        this.pword = pword;
        this.credits = 1000;
    }

    @Override
    public String toString() {
        return "User{" + "uname=" + uname + ", pword=" + pword + '}' + " Credits: "+credits;
    }

    /**
     * @return the uname
     */
    public String getUname() {
        return uname;
    }

    /**
     * @param uname the uname to set
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * @return the pword
     */
    public String getPword() {
        return pword;
    }

    /**
     * @param pword the pword to set
     */
    public void setPword(String pword) {
        this.pword = pword;
    }

    public void setCredits(int credits){this.credits = credits;}
    public void addCredits(int credits){this.credits = this.credits +credits;}
    public int getCredits(){return credits;}
}
