package edu.ufp.inf.sd.rmi.project.client;

import edu.ufp.inf.sd.rmi.project.server.SubjectRI;
import edu.ufp.inf.sd.rmi.project.server.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import java.util.Observer;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


/**
 * This class implements the Observer Remote Interface and has the working methods.
 *
 *
 *
 */
public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private User user;
    protected SubjectRI subjectRI;
    private ArrayList<String> hashes;
    private boolean pause = false;
    //Strat 2
    private ArrayList<String> myWords2;

    /**
     * This constructor initiates the class attributes and attaches the observer to a task.
     */
    protected ObserverImpl(SubjectRI subjectRI, User user) throws RemoteException, NoSuchAlgorithmException {
        super();
        this.user = user;
        this.myWords2 = new ArrayList<>();
        this.subjectRI = subjectRI;
        this.hashes = subjectRI.getState().getHashesToFind();

        this.subjectRI.attach(this);
    }

    /**
     * Updates an observer.
     *
     * @param typeOfUdpate
     * @param hf hashFound (Empty string if it's a different typeOfUpdate)
     */
    @Override
    public void update(String typeOfUdpate, String hf) throws RemoteException {
        if(typeOfUdpate.compareTo("WF") == 0)
            hashes.remove(hf);
        if(typeOfUdpate.compareTo("END") == 0){
            subjectRI.setEnd(true);
        }
        if(typeOfUdpate.compareTo("PAUSE") == 0){
            this.pause = true;
        }
    }

    /**
     * Work, strategy 1
     *
     */
    public void workOnTask1v2() throws NoSuchAlgorithmException, RemoteException {
        int count = 0;
        int currentLine = subjectRI.getCurrentLine();
        int delta = subjectRI.getDelta();

        subjectRI.setCurrentLine(currentLine + delta);

        //System.out.println("Size: " + hashes.size());

        String generatedPassword;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        try {
            TimeUnit.SECONDS.sleep(4);
            File myObj = new File("C:\\Users\\joaor\\IdeaProjects\\sd2020\\src\\edu\\ufp\\inf\\sd\\rmi\\project\\server\\darkcode.txt");

            Scanner myReader = new Scanner(myObj);
            for(int i = 0; i < currentLine; i++) myReader.nextLine();

            while (myReader.hasNextLine() && count + currentLine < delta + currentLine) {
                if(subjectRI.getEnd()){
                    System.out.println("All words found. Ending task.");
                    System.out.println();
                    return;
                }
                if(this.pause){
                    System.out.println("Pausing task for 10 seconds.");
                    TimeUnit.SECONDS.sleep(10);
                    this.pause = false;
                }

                //this.user.setCredits(this.user.getCredits() + 1);
                this.user.addCredits(1);

                String data = myReader.nextLine();

                count++;
                if(subjectRI.getState().getPasswordFixedLength() != data.length()) continue;
                System.out.println(count + ": " + data);

                byte[] bytes = md.digest(data.getBytes());
                StringBuilder sb = new StringBuilder();

                for(int i=0; i< bytes.length ;i++)
                {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                generatedPassword = sb.toString();

                for(int i = 0; i < hashes.size(); i++){
                    if(generatedPassword.compareTo(hashes.get(i)) == 0){
                        //this.user.setCredits(this.user.getCredits() + 10);
                        this.user.addCredits(10);
                        subjectRI.getState().getPasswordsMatched().add(data);
                        System.out.println("Word found: " + data);
                        subjectRI.setState("WF", hashes.get(i));
                    }
                }
                if(hashes.size() == 0){
                    subjectRI.setState("END","");
                }
            }

            myReader.close();
        } catch (FileNotFoundException | RemoteException | InterruptedException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        subjectRI.detach(this);
    }


    /**
     * Work, strategy 2
     *
     */
    public void workOnTask2v2() throws RemoteException, NoSuchAlgorithmException, FileNotFoundException, InterruptedException {

        String generatedPassword;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        if(hashes.size() == 0){
            System.out.println("This task has no more hashes to find.");
            return;
        }
        int count = 0;
        int currentLine = subjectRI.getCurrentLine();
        int delta = subjectRI.getDelta();
        int share [];
        byte[] bytes;
        StringBuilder sb;

        subjectRI.setCurrentLine(currentLine + delta);

        //All words found, ask server for a fair share (divided by all observers)
        //Share [0] has number of words to evaluate, Share[1] has blockNumber
        if(currentLine + delta > 1471051){
            subjectRI.setCurrentLine(1471051);
            myWords2 = subjectRI.getWordsV2();
            share = subjectRI.myShare();

            if(share[1] == 0){
                for(int i = 0; i < share[0]; i++){
                    if(subjectRI.getEnd()){
                        System.out.println("All words found. Ending task.");
                        return;
                    }
                    if(this.pause){
                        System.out.println("Pausing task for 10 seconds.");
                        TimeUnit.SECONDS.sleep(10);
                        this.pause = false;
                    }
                    String data = myWords2.get(i);
                    System.out.println(data);

                    bytes = md.digest(data.getBytes());
                    sb = new StringBuilder();
                    for(int j=0; j< bytes.length ;j++)
                    {
                        sb.append(Integer.toString((bytes[j] & 0xff) + 0x100, 16).substring(1));
                    }
                    generatedPassword = sb.toString();
                    this.user.setCredits(this.user.getCredits() + 1);

                    for(int k = 0; k < hashes.size(); k++){
                        if(generatedPassword.compareTo(hashes.get(k)) == 0){
                            this.user.setCredits(this.user.getCredits() + 10);
                            subjectRI.getState().getPasswordsMatched().add(data);
                            System.out.println("Word found: " + data);
                            subjectRI.setState("WF", hashes.get(k));
                        }
                    }

                }
            } else if (share[1] != 0){
                for(int i = share[0] * share[1]; i < share[0] * share[1] + share[0]; i++){
                    if(subjectRI.getEnd()){
                        System.out.println("All words found. Ending task.");
                        return;
                    }
                    if(this.pause){
                        System.out.println("Pausing task for 10 seconds.");
                        TimeUnit.SECONDS.sleep(10);
                        this.pause = false;
                    }
                    String data = myWords2.get(i);
                    System.out.println(data);

                    bytes = md.digest(data.getBytes());
                    sb = new StringBuilder();
                    for(int j=0; j< bytes.length ;j++)
                    {
                        sb.append(Integer.toString((bytes[j] & 0xff) + 0x100, 16).substring(1));
                    }
                    generatedPassword = sb.toString();
                    this.user.setCredits(this.user.getCredits() + 1);

                    for(int k = 0; i < hashes.size(); k++){
                        if(generatedPassword.compareTo(hashes.get(k)) == 0){
                            this.user.setCredits(this.user.getCredits() + 10);
                            subjectRI.getState().getPasswordsMatched().add(data);
                            System.out.println("Word found: " + data);
                            subjectRI.setState("WF", hashes.get(k));
                        }
                    }
                }
            }

        } else{
            System.out.println("Finding words of given length.");
            File myObj = new File("C:\\Users\\joaor\\IdeaProjects\\sd2020\\src\\edu\\ufp\\inf\\sd\\rmi\\project\\server\\darkcode.txt");

            Scanner myReader = new Scanner(myObj);
            for(int i = 0; i < currentLine; i++) myReader.nextLine();

            while (myReader.hasNextLine() && count + currentLine < delta + currentLine) {
                if(subjectRI.getEnd()){
                    System.out.println("All words found. Ending task.");
                    return;
                }
                if(this.pause){
                    System.out.println("Pausing task for 10 seconds.");
                    TimeUnit.SECONDS.sleep(10);
                    this.pause = false;
                }
                String data = myReader.nextLine();
                count++;

                if(subjectRI.getState().getPasswordFixedLength() != data.length()){
                    continue;
                } else{
                    myWords2.add(data);
                }
            }

            myReader.close();
            for(int i = 0; i < myWords2.size(); i++){
                subjectRI.addWordsV2(myWords2.get(i));
            }
        }
/*
        File myObj = new File("C:\\Users\\joaor\\IdeaProjects\\sd2020\\src\\edu\\ufp\\inf\\sd\\rmi\\project\\server\\darkcode.txt");

        Scanner myReader = new Scanner(myObj);
        for(int i = 0; i < currentLine; i++) myReader.nextLine();

        while (myReader.hasNextLine() && count + currentLine < delta + currentLine) {
            String data = myReader.nextLine();
            count++;

            if(subjectRI.getState().getPasswordFixedLength() != data.length()){
                continue;
            } else{
                myWords2.add(data);
            }
        }

        myReader.close();
        for(int i = 0; i < myWords2.size(); i++){
            subjectRI.addWordsV2(myWords2.get(i));
        }*/
    }

    /**
     * Work, strategy 3
     *
     */
    public void workOnTask3() throws RemoteException, NoSuchAlgorithmException, InterruptedException {
        int passLength = subjectRI.getState().getPasswordFixedLength();
        int currentCharNum = subjectRI.getCurrentLine();
        subjectRI.setCurrentLine(currentCharNum + 13);
        ArrayList<Character> chars = new ArrayList<>();
        int current = 'a' + currentCharNum;
        int start = current;

        while(current < start + 13){
            System.out.println((char)current);
            chars.add( (char)current );
            current++;
        }


        char [] startingSet = new char[chars.size()];
        char[] fullSet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's','t','u','v','w','x','y','z'};

        for(int i = 0; i < chars.size(); i++){
            startingSet[i] = chars.get(i);
        }
        System.out.println(startingSet);

        getAllKLength(startingSet,passLength,fullSet);

        subjectRI.detach(this);

    }

    /**
     * Starting point for algorithm strategy 3
     *
     */
    private void getAllKLength(char[] set, int k, char[] fullSet) throws RemoteException, NoSuchAlgorithmException, InterruptedException {

        int n = fullSet.length;
        ArrayList<Character> diffSet = checkSets(set,fullSet);
        getAllKLengthRec(set, "", n, k,fullSet,diffSet);
    }

    /**
     * Helper method for algorithm of strategy 3. It helps dividing the work.
     *
     */
    static ArrayList<Character> checkSets(char[] startSet, char[] fullset){
        StringBuilder sb = new StringBuilder();
        ArrayList<Character> al = new ArrayList<>();
        int dif = fullset.length - startSet.length; // dif = 1 (abc ab example)
        int count;
        for(int i = 0; i < fullset.length; i++){ //3 times: a,b,c
            count = 0;
            for(int j = 0; j < startSet.length; j++){ //2 times: a,b

                if (fullset[i] != startSet[j]){
                    count++;
                }
            }
            if(count == startSet.length ) sb.append(fullset[i]);
        }

        for(int i = 0; i < sb.length(); i++){
            al.add(sb.toString().charAt(i));
        }
        return al;
    }

    /**
     * Recursive algorithm to find all words within the given set.
     *
     */
    private void getAllKLengthRec(char[] set, String prefix, int n, int k, char[] fullSet, ArrayList<Character> diffSet) throws RemoteException, NoSuchAlgorithmException, InterruptedException {

        if(subjectRI.getEnd()){
            System.out.println("All words found. Ending task.");
            return;
        }
        if(this.pause){
            System.out.println("Pausing task for 10 seconds.");
            TimeUnit.SECONDS.sleep(10);
            this.pause = false;
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        //If first letter is in prefix is from de difSet, dont compute
        if(prefix.length() == 1 && diffSet.contains(prefix.charAt(0)))return;

        // Base case: k is 0,
        // Prefix = word
        if (k == 0)
        {
            System.out.println(prefix);
            this.user.setCredits(this.user.getCredits() + 1);

            //Now that we have the word, we calculate the Hash
            byte[] bytes = md.digest(prefix.getBytes());

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            String hashOfWord = sb.toString();
            //System.out.println(hashOfWord);

            for(int j = 0; j < hashes.size(); j++){
                //System.out.println(hashOfWord + " : " + hashes.get(j));
                if(hashOfWord.compareTo(hashes.get(j)) == 0){
                    this.user.setCredits(this.user.getCredits() + 10);
                    subjectRI.getState().getPasswordsMatched().add(prefix);
                    System.out.println("Word found: " + prefix);
                    subjectRI.setState("WF", hashes.get(j));
                }
            }
            return;
        }

        // One by one add all characters
        // from set and recursively
        // call for k equals to k-1
        for (int i = 0; i < n; ++i)
        {
            // Next character of input added
            String newPrefix = prefix + fullSet[i];

            // k is decreased, because
            // we have added a new character
            getAllKLengthRec(set, newPrefix, n, k - 1, fullSet,diffSet);
        }
    }

    public User getUser(){return user;}
}
