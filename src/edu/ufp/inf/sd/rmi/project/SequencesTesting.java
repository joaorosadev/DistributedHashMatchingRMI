package edu.ufp.inf.sd.rmi.project;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SequencesTesting {

    // Driver Code
    public static void main(String[] args) throws InterruptedException {
        System.out.println("First Test");
        char[] startSet = {'a','b'};
        char[] fullSet = {'a','b','c',};
        //K is word lenght
        int k = 3;
        SequencesTesting st = new SequencesTesting();
        //st.printAllKLength(startSet, k, fullSet);

        //System.out.println(checkSets(startSet,fullSet));

        System.out.println("\nSecond Test");
        char [] start2 = {'n', 'o', 'p', 'q', 'r',
                's','t','u','v','w','x','y','z'};
        char[] set2 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's','t','u','v','w','x','y','z'};
        k = 4;
        st.printAllKLength(start2,k,set2);


    }

    // The method that prints all
    // possible strings of length k.
    // It is mainly a wrapper over
    // recursive function printAllKLengthRec()
    void printAllKLength(char[] set, int k, char[] fullSet) throws InterruptedException {

        int n = fullSet.length;
        ArrayList<Character> al = checkSets(set,fullSet);
        System.out.println(al);
        printAllKLengthRec(set, "", n, k, fullSet,al);
    }

    // The main recursive method
    // to print all possible
    // strings of length k
    void printAllKLengthRec(char[] set, String prefix, int n, int k, char[] fullSet, ArrayList<Character> al) throws InterruptedException {



        //If first letter of prefix is from de difSet, dont compute
        //if(k < n - 1 && al.contains(prefix.charAt(0))) return;
        if(prefix.length() == 1 && al.contains(prefix.charAt(0)))return;

        // Base case: k is 0,
        // print prefix
        if (k == 0)
        {
            //if(al.contains(prefix.charAt(0))) return;
            System.out.println("word " +prefix);
            TimeUnit.SECONDS.sleep(1);
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
            printAllKLengthRec(set, newPrefix, n, k - 1,fullSet,al);
        }
    }

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

}

