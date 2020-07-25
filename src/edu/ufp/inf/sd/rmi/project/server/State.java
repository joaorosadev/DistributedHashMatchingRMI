package edu.ufp.inf.sd.rmi.project.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class holds all the parameters for a specific task.
 *
 *
 *
 */
public class State implements Serializable {
    private ArrayList<String> hashesToFind;
    private int passwordFixedLength;
    private ArrayList<String> passwordsMatched;
    private String strategy;

    /**
     * This is a helper constructor.
     */
    public State() {
        passwordFixedLength = 4;
        passwordsMatched = new ArrayList<>();
        hashesToFind = new ArrayList<>();
    }

    /**
     * This constructor initiates the class attributes.
     */
    public State(String strategy, int passwordFixedLength) throws FileNotFoundException {
        passwordsMatched = new ArrayList<>();
        hashesToFind = new ArrayList<>();
        this.passwordFixedLength = passwordFixedLength;
        this.strategy = strategy;

        addHashesToFind();
    }

    /**
     * Reads hashes from a text file and adds it to the task state.
     *
     */
    public void addHashesToFind() throws FileNotFoundException {
        if(strategy.compareTo("1") == 0 || strategy.compareTo("2") == 0 || strategy.compareTo("3") == 0){
            File myObj = new File("C:\\Users\\joaor\\IdeaProjects\\sd2020\\src\\edu\\ufp\\inf\\sd\\rmi\\project\\server\\hashes2.txt");
            //File myObj = new File("C:\\Users\\joaor\\IdeaProjects\\sd2020\\src\\edu\\ufp\\inf\\sd\\rmi\\project\\server\\strat1hashes.txt");
            Scanner myReader = new Scanner(myObj);
            while(myReader.hasNextLine()){
                hashesToFind.add(myReader.nextLine());
            }
            myReader.close();
        }
    }

    public int getPasswordFixedLength(){ return passwordFixedLength;}
    public ArrayList<String> getHashesToFind(){return hashesToFind;}
    public ArrayList<String> getPasswordsMatched(){ return passwordsMatched;}
    public String getStrategy(){return strategy;}
}
