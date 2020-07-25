package edu.ufp.inf.sd.rmi.project.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface has the remote Observer methods.
 *
 *
 *
 */
public interface ObserverRI extends Remote {
    public void update(String typeOfUdpate, String hf) throws RemoteException;
}
