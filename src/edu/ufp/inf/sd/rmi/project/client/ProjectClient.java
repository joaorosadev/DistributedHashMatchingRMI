package edu.ufp.inf.sd.rmi.project.client;

import edu.ufp.inf.sd.rmi.project.server.*;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the starting point for clients. It has the interactions menus.
 *
 *
 *
 */
public class ProjectClient extends Thread {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;

    /**
     * Remote interfaces that will hold the Servant proxy
     */
    FactoryRI factoryRI;
    ObserverImpl observer;
    //boolean firstTimeJoiningTask = true;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.helloworld.server.CalculatorClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            ProjectClient projCl = new ProjectClient(args);
            //2. ============ Lookup service ============
            projCl.lookupService();
            //3. ============ Play with service ============
            //projCl.playService(args);
            projCl.start();
        }
    }

    public ProjectClient(String args[]) {
        try {
            //List ans set args
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(ProjectClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Lookup for the Factory remote interface
     *
     */
    private Remote lookupService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);

                //============ Get proxy to HelloWorld service ============
                factoryRI = (FactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return factoryRI;
    }

    private void playService(String args[]){
        try{
            SessionInfo sessionInfo = startMenu();
            SessionRI sessionRI = sessionInfo.getSessionRI();

            if(sessionRI != null){
                System.out.println("Successfull login.");
                System.out.println();
                tasksMenu(sessionInfo);

            } else{
                System.out.println("The username or password are incorrect.");
            }

        } catch (IOException | NoSuchAlgorithmException | InterruptedException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Login menu
     *
     */
    private SessionInfo startMenu() throws IOException {
        String username;
        String pw;
        SessionRI session = null;
        SessionInfo sessionInfo = null;

        System.out.println("1 - Register and Login");
        System.out.println("2 - Login");

        //SCAN FOR OPTION
        Scanner myObj = new Scanner(System.in);
        String opt = myObj.nextLine();
        clearScreen();

        switch (opt){
            case "1":
                System.out.print("Username: ");
                username = myObj.nextLine();
                System.out.print("Password: ");
                pw = myObj.nextLine();
                factoryRI.register(username,pw);
                session = factoryRI.login(username,pw);
                sessionInfo = new SessionInfo(session,username);
                clearScreen();
                break;
            case "2":
                System.out.print("Username: ");
                username = myObj.nextLine();
                System.out.print("Password: ");
                pw = myObj.nextLine();
                session = factoryRI.login(username,pw);
                sessionInfo = new SessionInfo(session,username);
                clearScreen();
                break;
        }
        return sessionInfo;
    }

    /**
     * Tasks menu
     *
     */
    private void tasksMenu(SessionInfo sessionInfo) throws RemoteException, NoSuchAlgorithmException, FileNotFoundException, InterruptedException {
        int currentCreds=0;
        for(User u: factoryRI.getDb().getUsers()){
            if(u.getUname().compareTo(sessionInfo.getUsername())==0){
                currentCreds = u.getCredits();
            }
        }
        System.out.println("User: " + sessionInfo.getUsername()+"\tCredits: " + currentCreds+"\n");
        System.out.println("1 - Create task");
        System.out.println("2 - List tasks");
        System.out.println("3 - Join task");
        System.out.println("4 - Delete task");
        System.out.println("5 - Pause task");
        System.out.println("6 - Logout");

        Scanner myObj = new Scanner(System.in);
        String opt = myObj.nextLine();
        clearScreen();

        switch (opt){
            //CREATE TASK
            case "1":
                System.out.println("What strategy do you want to use? (1, 2 or 3)");
                String opt2 = myObj.nextLine();

                if(opt2.compareTo("1") == 0 || opt2.compareTo("2") == 0 || opt2.compareTo("3") == 0){
                    System.out.println("Length of the password?");
                    int fixLength = myObj.nextInt();
                    sessionInfo.getSessionRI().createTask(sessionInfo.getUsername(),opt2, fixLength);
                }
                //Pay 100 credits to create task
                for(User u: factoryRI.getDb().getUsers()){
                    if(u.getUname().compareTo(sessionInfo.getUsername()) == 0){
                        u.setCredits(u.getCredits() - 100);
                    }
                }
                System.out.println();
                tasksMenu(sessionInfo);
                break;
            //LIST TASKS
            case "2":
                ArrayList<String> tasks = sessionInfo.getSessionRI().listTasks();
                System.out.println("Tasks: ");

                int i = 0;
                for(String s: tasks){
                    System.out.println("Id in array (use to delete): " + i + "\tTask id: " + s);
                    i++;
                }
                System.out.println();
                tasksMenu(sessionInfo);
                break;
            //JOIN TASK
            case "3":
                sessionInfo.getSessionRI().listTasks();
                System.out.println("What task do you want to join?");
                int taskId = myObj.nextInt();


                for(User user: factoryRI.getDb().getUsers()){
                    if(user.getUname().compareTo(sessionInfo.getUsername()) == 0){
                        //I have to check if the user has already worked on the task to not create another observer
                        //System.out.println("TESTE: " + ((SubjectRI)factoryRI.getDb().getTasks().get(taskId)).getObservers());
                        //TimeUnit.SECONDS.sleep(4);
                        /*
                        if( ((SubjectRI)factoryRI.getDb().getTasks().get(taskId)).getObservers().size() == 0 || firstTimeJoiningTask){
                            observer = new ObserverImpl((SubjectRI)factoryRI.getDb().getTasks().get(taskId),user);
                            firstTimeJoiningTask = false;
                        }

                        for(ObserverRI observerRI: ((SubjectRI)factoryRI.getDb().getTasks().get(taskId)).getObservers()){
                            //System.out.println(((ObserverImpl)observerRI).getUser().getUname() +user.getUname() );
                            TimeUnit.SECONDS.sleep(4);
                            if( observerRI.getUser().getUname().compareTo(user.getUname()) == 0 ){
                                observer = observerRI;
                            }
                        }
                         */


                        observer = new ObserverImpl(factoryRI.getDb().getTasks().get(taskId),user);

                        String strategy = ((SubjectRI)factoryRI.getDb().getTasks().get(taskId)).getState().getStrategy();
                        if(strategy.compareTo("1") == 0){
                            observer.workOnTask1v2();
                            //observer.workOnTask1Threaded();
                        } else if (strategy.compareTo("2") == 0){
                            observer.workOnTask2v2();
                        } else  if (strategy.compareTo("3") == 0){
                            observer.workOnTask3();
                        }


                        tasksMenu(sessionInfo);
                    }
                }
                break;
            //DELETE TASK
            case "4":
                System.out.print("Which task do you want to delete? ");
                int id = Integer.parseInt(myObj.nextLine());
                sessionInfo.getSessionRI().deleteTask(id,sessionInfo.getUsername());
                System.out.println();
                tasksMenu(sessionInfo);
                break;
            //PAUSE TASK
            case "5":
                System.out.println("Which task do you want to pause? ");
                int task = myObj.nextInt();
                ((SubjectRI)factoryRI.getDb().getTasks().get(task)).setState("PAUSE","");
                tasksMenu(sessionInfo);
                break;
            //LOGOUT
            case "6":
                sessionInfo.getSessionRI().logout(sessionInfo.getUsername());
                break;
        }
    }

    /**
     * Helper function
     *
     */
    private static void clearScreen(){
        for(int i=0; i<25; i++)
            System.out.println();
    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }

    public void run(){
        String args[] = {};
        playService(args);
    }

}
