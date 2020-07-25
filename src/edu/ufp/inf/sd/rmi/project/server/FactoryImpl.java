package edu.ufp.inf.sd.rmi.project.server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class implements the Factory Remote Interface.
 *
 *
 *
 */
public class FactoryImpl extends UnicastRemoteObject implements FactoryRI {

    private DBMockup db;
    private HashMap<String, SessionRI> sessions;
    private LinkedList<SubjectTaskImpl> tasks;

    /**
     * This constructor initiates the class attributes.
     */
    protected FactoryImpl() throws RemoteException {
        super();
        db = DBMockup.getDbInstance();
        sessions = new HashMap<>();
        tasks = new LinkedList<>();
    }

    /**
     * Register a user.
     *
     * @param uname username
     * @param pw password
     */
    @Override
    public boolean register(String uname, String pw){
        if(!db.exists(uname,pw)){
            db.register(uname,pw);
            System.out.println("\nUser \"" + uname + "\" registered.");
            return true;
        }
        System.out.println("\nThe username \"" + uname + "\" already exists.");
        return false;
    }

    /**
     * Login a user.
     *
     * @param uname username
     * @param pw password
     */
    @Override
    public SessionRI login(String uname, String pw) throws RemoteException{

        if(db.exists(uname,pw)){
            if(!sessions.containsKey(uname)){
                SessionRI session = new SessionImpl(this);
                this.sessions.put(uname,session);
                System.out.println("\nUser \"" + uname + "\" logged in.");
                return session;
            }else{
                System.out.println("\nUser \"" + uname + "\" already logged in.");
                return sessions.get(uname);
            }
        }
        System.out.println("\nThe username or password are incorrect.");
        return null;
    }

    @Override
    public DBMockup getDb() {
        return db;
    }
    public HashMap<String, SessionRI> getSessions() {
        return sessions;
    }
    @Override
    public LinkedList<SubjectTaskImpl> getTasks(){return tasks;}

}
