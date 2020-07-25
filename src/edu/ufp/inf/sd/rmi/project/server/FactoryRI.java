package edu.ufp.inf.sd.rmi.project.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * This interface has the factory remote methods.
 *
 *
 *
 */
public interface FactoryRI extends Remote {
    public SessionRI login(String uname, String pw) throws RemoteException;
    public boolean register(String uname, String pw) throws RemoteException;
    public DBMockup getDb() throws  RemoteException;
    public LinkedList<SubjectTaskImpl> getTasks() throws RemoteException;
}
