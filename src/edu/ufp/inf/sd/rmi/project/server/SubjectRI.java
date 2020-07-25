package edu.ufp.inf.sd.rmi.project.server;

import edu.ufp.inf.sd.rmi.project.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This interface has all the Subject (task group) remote methods.
 *
 *
 *
 */
public interface SubjectRI extends Remote {
    public void attach(ObserverRI obsRI) throws RemoteException;
    public void detach(ObserverRI obsRI) throws RemoteException;
    public State getState() throws RemoteException;
    public void setState(State state, String typeOfUpdate, String hf) throws RemoteException;
    public void setState(String typeOfUpdate, String hf) throws RemoteException;
    public Integer getCurrentLine() throws RemoteException;
    public Integer getDelta() throws RemoteException;
    public void setCurrentLine(Integer i) throws RemoteException;
    public void setWordsV2(ArrayList<String> words) throws RemoteException;
    public ArrayList<String> getWordsV2() throws RemoteException;
    public void addWordsV2(String word) throws RemoteException;
    public int [] myShare() throws RemoteException;
    //Pause and End methods
   // public void setPaused(boolean paused) throws RemoteException;
    //public boolean getPaused() throws RemoteException;
    public void setEnd(boolean end) throws RemoteException;
    public boolean getEnd() throws RemoteException;

}
