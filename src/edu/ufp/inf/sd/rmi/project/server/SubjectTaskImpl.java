package edu.ufp.inf.sd.rmi.project.server;

import edu.ufp.inf.sd.rmi.project.client.ObserverRI;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class implements the Subject Remote interface.
 *
 *
 *
 */
public class SubjectTaskImpl extends UnicastRemoteObject implements SubjectRI {
    private static final AtomicInteger count = new AtomicInteger(0);
    private static final AtomicInteger count2 = new AtomicInteger(0);
    private int taskId;
    private String owner;
    private State taskState;
    private final ArrayList<ObserverRI> observers = new ArrayList<>();
    private static Integer currentLine;
    private Integer delta;
    //Pause and End task
    private boolean paused;
    private boolean end;
    //For strat 2
    private static ArrayList<String> wordsV2;
    private static int blockNumber = 0;


    /**
     * This is a secondary constructor
     */
    protected SubjectTaskImpl(String owner) throws RemoteException, NoSuchAlgorithmException {
        super();
        this.taskState = new State();
        this.taskId = count.incrementAndGet();
        this.owner = owner;
        currentLine = 0;
        delta = 10000; //250000
    }

    /**
     * This constructor initiates the class attributes.
     */
    protected SubjectTaskImpl(String owner, int fixLength, String strategy) throws RemoteException, FileNotFoundException, InterruptedException {
        super();
        this.taskState = new State(strategy, fixLength);
        this.taskId = count.incrementAndGet();
        this.owner = owner;
        currentLine = 0;
        delta = 250000;
        wordsV2 = new ArrayList<>();
        paused = false;
        end = false;
    }

    /**
     * Attaches an observer to this task.
     *
     */
    @Override
    public void attach(ObserverRI obsRI) throws RemoteException {
        if (!observers.contains(obsRI)) {
            observers.add(obsRI);
            //obsRI.setWorkerId(count2.incrementAndGet());
        }
    }

    /**
     * Dettaches an observer to this task.
     *
     */
    @Override
    public void detach(ObserverRI obsRI) throws RemoteException {
        if (observers.contains(obsRI))
            observers.remove(obsRI);
    }

    /**
     * Gets the state of the task.
     *
     */
    @Override
    public State getState() throws RemoteException {
        return taskState;
    }

    /**
     * Changes the state of the task
     *
     * @param typeOfUpdate ("END", "PAUSE" or "WF" (Word Found))
     * @param hf hashfound
     *
     */
    @Override
    public void setState(State state, String typeOfUpdate, String hf) throws RemoteException {
        this.taskState = state;
        this.notifyAllObservers(typeOfUpdate, hf);
    }

    /**
     * Changes the state of the task
     *
     * @param typeOfUpdate ("END", "PAUSE" or "WF" (Word Found))
     * @param hf hashfound
     *
     */
    @Override
    public void setState(String typeOfUpdate, String hf) throws RemoteException {
        if(hf.compareTo("") != 0)
            taskState.getHashesToFind().remove(hf);

        this.notifyAllObservers(typeOfUpdate, hf);
    }

    /**
     * Notifyies all the observers attached to this task.
     *
     * @param typeOfUpdate ("END", "PAUSE" or "WF" (Word Found))
     * @param hf hashfound
     *
     */
    public void notifyAllObservers(String typeOfUpdate, String hf) throws RemoteException {
        for (ObserverRI obs : observers) {
            obs.update(typeOfUpdate, hf);
        }
    }

    public String getOwner() {
        return owner;
    }

    public int getId() {
        return taskId;
    }

    @Override
    public String toString() {
        return owner;
    }

    @Override
    public Integer getCurrentLine() {
        return currentLine;
    }

    @Override
    public Integer getDelta() {
        return delta;
    }

    @Override
    public void setCurrentLine(Integer currentLine) {
        SubjectTaskImpl.currentLine = currentLine;
    }

    @Override
    public void setWordsV2(ArrayList<String> words){wordsV2 = words;}

    @Override
    public ArrayList<String> getWordsV2(){return wordsV2;}

    @Override
    public void addWordsV2(String word){
        wordsV2.add(word);
    }


    @Override
    public void setEnd(boolean end) throws RemoteException {
        this.end = end;
    }
    /*@Override
    public boolean getPaused(){return paused;}
    */
    @Override
    public boolean getEnd(){return end;}

    /**
     * Gives a fair share of words to an observer (strat 2)
     *
     */
    @Override
    public int [] myShare(){
        int share [] = {0,0};
        share[0] = wordsV2.size()/observers.size();
        share[1] = blockNumber++;
        return share;
    }
}
