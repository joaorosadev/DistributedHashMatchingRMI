package edu.ufp.inf.sd.rmi.project.client;

import edu.ufp.inf.sd.rmi.project.server.DBMockup;
import edu.ufp.inf.sd.rmi.project.server.SessionRI;

import java.rmi.RemoteException;

/**
 * This class holds is an utility class. It holds the username and session of a user in a single object.
 *
 *
 *
 */
public class SessionInfo {
    private String username;
    private SessionRI sessionRI;
    private DBMockup db;

    /**
     * This constructor initiates the class attributes.
     */
    public SessionInfo(SessionRI sessionRI, String username) throws RemoteException {
        super();
        this.sessionRI = sessionRI;
        this.username = username;
        db = DBMockup.getDbInstance();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SessionRI getSessionRI() {
        return sessionRI;
    }

    /*public void setSessionRI(SessionRI sessionRI) {
        this.sessionRI = sessionRI;
    }

    public DBMockup getDb(){return db;}*/
}
