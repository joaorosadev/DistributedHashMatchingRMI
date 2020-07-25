package edu.ufp.inf.sd.rmi.project.server;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * This class implements methods of the Session Remote Interface. It deals with creation/deletion/listing of tasks.
 *
 *
 *
 */
public class SessionImpl extends UnicastRemoteObject implements SessionRI {

    //private DBMockup db;
    private FactoryImpl factoryImpl;


    /**
     * This constructor initiates the class attributes.
     */
    protected SessionImpl(FactoryImpl factoryImpl) throws RemoteException {
        super();
        //this.db = DBMockup.getDbInstance();
        this.factoryImpl = factoryImpl;
    }

    /**
     * Logout a user.
     *
     * @param uname username
     *
     */
    @Override
    public void logout(String uname) throws RemoteException {
        if(this.factoryImpl.getSessions().containsKey(uname)){
            this.factoryImpl.getSessions().remove(uname);
            System.out.println();
            System.out.println("User: \"" + uname +"\" logged out.");
            System.out.println();
        }
    }

    /**
     * Creates a task.
     *
     * @param username
     * @param strategy (1, 2 or 3)
     * @param length size of the words
     *
     */
    @Override
    public SubjectRI createTask(String username, String strategy, int length) throws RemoteException, NoSuchAlgorithmException, FileNotFoundException, InterruptedException {
        SubjectRI task;

        for(User u: factoryImpl.getDb().getUsers()) {
            if (u.getUname().compareTo(username) == 0) {
                u.setCredits(u.getCredits() - 100);
            }
        }
        if(strategy.compareTo("") == 0 && length == -1){
            task = new SubjectTaskImpl(username);
            this.factoryImpl.getTasks().add((SubjectTaskImpl)task);
            this.factoryImpl.getDb().addTask((SubjectTaskImpl)task);

        } else{
            task = new SubjectTaskImpl(username,length,strategy);
            this.factoryImpl.getTasks().add((SubjectTaskImpl)task);
            this.factoryImpl.getDb().addTask((SubjectTaskImpl)task);
        }

        return task;
    }

    /**
     * Lists all tasks
     *
     */
    @Override
    public ArrayList<String> listTasks() throws RemoteException {
        ArrayList<String> tasks = new ArrayList<>();
        for(SubjectTaskImpl task: factoryImpl.getTasks()){
            tasks.add(String.valueOf(task.getId()));
        }
        return tasks;
    }

    /**
     * Deletes a task.
     *
     * @param id of the task
     * @param userTryingToDeleteTask (needed to prevent another user deleting your task)
     *
     */
    @Override
    public void deleteTask(int id, String userTryingToDeleteTask) throws RemoteException {
        if(id  <= factoryImpl.getTasks().size()  && userTryingToDeleteTask.compareTo(factoryImpl.getTasks().get(id).getOwner()) == 0 )
            factoryImpl.getTasks().remove(id);
    }

    //@Override
    //public void pauseTask() throws RemoteException {

    //}
}
