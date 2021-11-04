import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

/*Member 
1. Warunyupa  Lerdsaeng        6313180
2. Nalin      Suesangiamsakul  6313216*/

class Factory extends Thread{
    private int ID,lotSize,countLots;
    private String product;
    private ArrayList<Integer> requriedMaterial;
    //private ArrayList<OneShareMaterial> OneShareArray;
    MyUtility program = new MyUtility();

    public Factory(){}
    public Factory(int id,String p,int l,ArrayList<Integer> rMaterial){
        super(p);
        ID = id;
        product = p;
        lotSize = l;
        requriedMaterial = rMaterial; 
        countLots = 0;
    }
    public void run(){
        //Not-complete
    }

    //Not-Complete 
    public void printFactory(){
        //Show detail Factory from read spec.txt 
        program.printThreadName();
        System.out.printf(" >> %-10s factory\t%4d units per lot materials per lot = ",product,lotSize);
        
        /* Show required Material 
        for(int i=0 ;i<requriedMaterial.size();i++){
            System.out.printf(" %3d %s,"requriedMaterial.get(i));
        }
        */
        System.out.println("");
    }
}//end Factory
class OneShareMaterial{
    private String name;
    private int balance,supplierPut;
    MyUtility program = new MyUtility();

    public OneShareMaterial(){}
    public OneShareMaterial(String n,int b){
        name = n;
        balance = b;  supplierPut = b;
    }
    public String getNameMaterial(){
        //ask Name Material
        return name;
    }
    public int getBalance(){
        //ask Balance Material
        return balance;
    }
    public void putMaterial(){
        //suplier add Material per day
        balance += supplierPut;
    }
    synchronized public int getMaterial(int num){
        //factory get Material
        int numGet=0; //numGet = factory can material

        if(balance - num >0){
            numGet = num;
            balance-=num;
        }
        else if(balance-num <=0 ){
            numGet = balance;
            balance = 0;
        }
        return numGet;
    }

    public void printListMaterial(){
        //check all list Materials
        program.printThreadName();
        System.out.printf(" >> Put %4d %s \tBalance = %4d %s\n",supplierPut,name,balance,name);
    }
}//end OneShareMaterial

class FactorySimulation {
    public static void main(String[] args) {
        MyUtility program = new MyUtility();
        ArrayList<OneShareMaterial> material = new ArrayList<OneShareMaterial>(); //ArrayList is used when we don't know exact value
        //*ArrayList<Factory> factory = new ArrayList<Factory>;
        Boolean openSuccess = false, readLine1 = false;
        Scanner scanInput = new Scanner(System.in);

        String fileName;
        int factID=0, upl;
        ArrayList<Integer> matRequired = new ArrayList<Integer>();
        ArrayList<String> matName = new ArrayList<String>();
        ArrayList<String> prodName = new ArrayList<String>();
        int matAdd = 0, days = 0;


        while (openSuccess == false) {
            program.printThreadName();
            System.out.printf(" >> Enter product specification file = \n");
            fileName = scanInput.next();

            try (Scanner scanFile = new Scanner(new File(fileName))) {
                openSuccess = true;
                while (scanFile.hasNext()) { // Read 1 line per round

                    String line = scanFile.nextLine();
                    String[] buf = line.split(",");
                    if (readLine1 == false) {
                        for(int i=0; i< buf.length; i++){
                            matName.add(buf[i]);
                        }
                        readLine1 = true;
                    } else {
                        factID = Integer.parseInt(buf[0].trim());
                        prodName.add(buf[1]);
                        upl = Integer.parseInt(buf[2].trim());
                        for(int i= 3; i< matName.size() + 3; i++){
                            matRequired.add(Integer.parseInt(buf[i].trim()));
                        }
                    }
                }
            }//end try
            catch (FileNotFoundException e) {
                System.out.println("File missing");
                System.err.println(e);
            } catch (RuntimeException e) {
                System.err.println(e);
            }
        } // end loop openFile

        while(matAdd == 0){
            try {
                program.printThreadName();
                System.out.printf(" >> Enter amount of material per day\n");
                matAdd = Integer.parseInt(scanInput.next());
            } catch (RuntimeException e) {
                System.err.println("Invalid input. \n" + e);
            }
        }//end read material per day input

        while (days == 0) {
            try {
                program.printThreadName();
                System.out.printf(" >> Enter number of days\n");
                days = Integer.parseInt(scanInput.next());
            } catch (RuntimeException e) {
                System.err.println("Invalid input. \n" + e);
            }
        }//end read days input

        for(int d=0; d<days; d++){
            program.printThreadName();
            System.out.printf(" >> Day %d\n", d);
            for(int i=0; i < factID; i++){
                material.add(new OneShareMaterial(prodName.get(i), matAdd));
                material.get(i).printListMaterial();
            }
            //*Wait for thread code
        }
        
        scanInput.close();
    }// end main    
}// end FactorySimulation

class MyUtility{
    public void printThreadName() {
        System.out.printf("Thread %-5s", Thread.currentThread().getName());
    }
}