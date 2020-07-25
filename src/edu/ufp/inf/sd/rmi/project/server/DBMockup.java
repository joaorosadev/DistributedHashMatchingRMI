package edu.ufp.inf.sd.rmi.project.server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and tasks.
 *
 *
 *
 */
public class DBMockup implements Serializable {

    private static DBMockup db;
    private final ArrayList<User> users;
    private final ArrayList<SubjectTaskImpl> tasks;

    /**
     * This constructor creates and inits the database with some books and users.
     */
    private DBMockup() {
        users = new ArrayList<>();
        tasks = new ArrayList<>();
        //Add one user
        users.add(new User("guest", "ufp"));
    }

    public static synchronized DBMockup getDbInstance(){
        if(db == null){
            db = new DBMockup();
        }
        return db;
    }

    /**
     * Registers a new user.
     * 
     * @param u username
     * @param p passwd
     */
    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
        }
    }

    public void addTask(SubjectTaskImpl task){
        if(!this.tasks.contains(task)){
            this.tasks.add(task);
        }
    }

    /**
     * Checks the credentials of an user.
     * 
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public ArrayList<SubjectTaskImpl> getTasks(){return tasks;}

    public ArrayList<User> getUsers(){return users;}

}
