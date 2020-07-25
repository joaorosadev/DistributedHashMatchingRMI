package edu.ufp.inf.sd.rmi.project;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class HashingTest {

    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String args[]) throws NoSuchAlgorithmException {

        //STRAT 1 WORDS:
        //0000
        //009b
        //0127
        //0412
        //oort
        //scuz
        //zzzz

        //File 2 WORDS:
        //0000
        //1770
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String passwordToHash = "1770";
        String generatedPassword;


        byte[] bytes = md.digest(passwordToHash.getBytes());
        StringBuilder sb = new StringBuilder();

        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
        System.out.println(generatedPassword);
        //"password"- 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8
        //"password1"-0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e
        //"copa" - 6dd4a33a44b9b1fac83291b80a45431e89ab90541e8654ae63281246958a8b3a
        //"dado" - 1bb2d0a82ece88e6e9e32e1d33b1539afc65ad548ee7f112177f51c0c94fbdb9
        //"zico" - c07af124eb1998528f972580429a91b046c24485ebb7ff21354a87c79c7f7790
        System.out.println((int)'a');
        System.out.println((int)'z');
        System.out.println(giveSubSet(4,1));
        System.out.println(giveSubSet(4,2));
        System.out.println(giveSubSet(4,3));
        System.out.println(giveSubSet(4,4));

    }


    private static char[] giveSubSet(int nWorkers, int workerId){
        int fairShare = 26/nWorkers;
        int start = 'a' + ((workerId-1) * fairShare);
        int current = start;
        ArrayList<Character> chars = new ArrayList<>();

        if(workerId == nWorkers){
            while(current <= 122){
                chars.add( (char)current );
                current++;
            }
        }else{
            while(current < start + fairShare){
                chars.add( (char)current );
                current++;
            }
        }

        char [] arr = new char[chars.size()];

        for(int i = 0; i < chars.size(); i++){
            arr[i] = chars.get(i);
        }

        return arr;
    }



}
