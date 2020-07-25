package edu.ufp.inf.sd.rmi.project.server;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * This interface holds the Session Remote methods.
 *
 *
 *
 */
public interface SessionRI extends Remote {
    public void logout(String uname) throws RemoteException;
    public SubjectRI createTask(String username, String strategy, int length) throws RemoteException, NoSuchAlgorithmException, FileNotFoundException, InterruptedException;
    public ArrayList<String> listTasks() throws RemoteException;
    public void deleteTask(int id, String userTryingToDeleteTask) throws RemoteException;
}
