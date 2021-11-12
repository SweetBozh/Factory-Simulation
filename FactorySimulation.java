/*Member 
1. Warunyupa  Lerdsaeng        6313180
2. Nalin      Suesangiamsakul  6313216*/

import java.io.*;
import java.util.*;

class Factory implements Runnable {
    private int ID, lotSize, countLots, fail = 0;
    // private String product;
    private ArrayList<Integer> requireMat, inStockMat = new ArrayList<Integer>();
    private ArrayList<OneShareMaterial> sharedMaterial;
    private MyUtility program = new MyUtility();

    public Factory() {
    }

    public Factory(int id, int ls, ArrayList<Integer> rMat, ArrayList<OneShareMaterial> onl, int nl,
            ArrayList<Integer> sMat) {
        ID = id; // product = p;
        lotSize = ls;
        requireMat = rMat;
        sharedMaterial = onl;
        countLots = nl;
        inStockMat = sMat;
    }

   public void run(){
          int facGet;
          for(int i=0;i<requireMat.size();i++){
              int facRequireToday,currentMat,facRequire= lotSize*requireMat.get(i);
              
              if(inStockMat.get(i)==0){  //when Stock has no material
                  facRequireToday = lotSize*requireMat.get(i); 
              }
              else{ //when have some material in Stock
                  facRequireToday = lotSize*requireMat.get(i) - inStockMat.get(i); 
              }
              if(inStockMat.get(i)<=facRequire && facRequireToday!=0 ){
                   
                    facGet = sharedMaterial.get(i).getMaterial(facRequireToday);
                    if(inStockMat.size()==requireMat.size()){
                        currentMat = inStockMat.get(i);
                        currentMat = currentMat + facGet;
                        inStockMat.set(i, currentMat);
                    }
                    else{
                        inStockMat.add(facGet);
                    }
                    if(inStockMat.get(i)< facRequire ){
                        fail++;
                    }
              }//end if
         
         }//end for
        
          if(fail!=0){
              System.out.printf("Thread %-9s >> -------- Fail\n",Thread.currentThread().getName());
          }
          else{
              int facRequireToday,currentMat = 0;
              for(int i =0;i<requireMat.size();i++){ //get material from stock to producing
                facRequireToday = lotSize*requireMat.get(i);
                currentMat = facRequireToday-inStockMat.get(i);
                inStockMat.set(i,currentMat);
              }
              countLots++;
              fail=0;
              System.out.printf("Thread %-9s >> -------- Complete Lot %d \n",Thread.currentThread().getName(),countLots);
          }
    }//end run
    
    public void printGet(int facGet, int m){
        program.printThreadName();
        System.out.printf(" >> Get %,5d %10s", facGet, sharedMaterial.get(m).getNameMaterial());
        System.out.printf("  Balance = %,5d %10s\n", sharedMaterial.get(m).getBalance(), sharedMaterial.get(m).getNameMaterial());
    }

    public int getNumberOfLot() {
        return countLots;
    }

    public ArrayList<Integer> getMaterialLeft() {
        return inStockMat;
    }

    public int getID(){
        return ID;
    }
}// end Factory


class OneShareMaterial {
    private String name;
    private int balance, supplierPut;
    private MyUtility program = new MyUtility();

    public OneShareMaterial() {
    }

    public OneShareMaterial(String n, int b) {
        name = n;
        supplierPut = b;
        balance = 0;
    }

    public String getNameMaterial() {
        // ask Name Material
        return name;
    }

    public int getBalance() {
        // ask Balance Material
        return balance;
    }

    public void putMaterial() {
        // suplier add Material per day
        balance += supplierPut;
    }

    synchronized public int getMaterial(int num) {
        // factory get Material
        int numGet = 0; // numGet = factory can material

        if (balance - num > 0) {
            numGet = num;
            balance -= num;
        } else if (balance - num <= 0) {
            numGet = balance;
            if (balance - num == 0)
                balance -= num;
            else
                balance = 0;
        }
        System.out.printf("Thread %-9s >> Get %,5d %10s \tBalance = %,5d %10s\n",Thread.currentThread().getName(),numGet,name,balance,name);
        return numGet;
    }

    public void printListMaterial() {
        // check all list Materials
        program.printThreadName();
        System.out.printf(" >> Put %,5d %10s \tBalance = %,5d %10s\n", supplierPut, name, balance, name);
    }
}// end OneShareMaterial

class FactorySimulation{
    public static void main(String[] args) {
        //----------------------------------- Local Variable --------------------------------
        MyUtility program = new MyUtility();
        ArrayList<OneShareMaterial> material = new ArrayList<OneShareMaterial>(); // ArrayList is used when we don't know exact value
        Boolean openSuccess = false, readLine1 = false;
        Scanner scanInput = new Scanner(System.in);

        String fileName;
        int factID = 0;
        // 2D ArrayList facRequired keep ArrayList of each type of materials required amount;
        // 2D ArrayList facMatLeft keep material left of each factory before thread die.
        ArrayList<ArrayList<Integer>> facRequired = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> facMatLeft = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> matName = new ArrayList<String>();
        ArrayList<String> prodName = new ArrayList<String>();
        ArrayList<Integer> upl = new ArrayList<Integer>();
        ArrayList<Integer> numberOfLot = new ArrayList<Integer>();
        int matAdd = 0, days = 0;

        System.out.printf("\n===============================================================================\n");
        System.out.printf("                    -=*=*!  Welcome to Factory Simulator !*=*=-                    ");
        System.out.printf("\n===============================================================================\n\n");
        //-------------------------- Open File & keep data to variable ----------------------
        while (openSuccess == false) {
            program.printThreadName();
            System.out.printf(" >> Enter product specification file = \n");
            fileName = scanInput.next();

            try (Scanner scanFile = new Scanner(new File(fileName))) {
                openSuccess = true;
                while (scanFile.hasNext()) { // Read 1 line per round

                    ArrayList<Integer> matRequired = new ArrayList<Integer>();
                    ArrayList<Integer> matLeft = new ArrayList<Integer>();

                    String line = scanFile.nextLine();
                    String[] buf = line.split(",");
                    if (readLine1 == false) {
                        for (int i = 0; i < buf.length; i++) {
                            matName.add(buf[i].trim());
                        }
                        readLine1 = true;
                    } else {
                        factID = Integer.parseInt(buf[0].trim());
                        prodName.add(buf[1].trim());
                        upl.add(Integer.parseInt(buf[2].trim()));
                        for (int i = 3; i < matName.size() + 3; i++) {
                            // Add Amount of each type of materials requirement and keep in Array matRequired
                            matRequired.add(Integer.parseInt(buf[i].trim()));
                            matLeft.add(0); // Set initial value;
                        }
                        facRequired.add(matRequired); // Example - There are 3 factory; pants shirt jeans, but each
                                                      // required 2 material; buttons, zippers
                        facMatLeft.add(matLeft);
                    }
                }System.out.printf("-------------------------------------------------------------------------------\n");
            } // end try
            catch (FileNotFoundException e) {
                System.out.println("File missing");
                System.err.println(e);
            } catch (RuntimeException e) {
                System.err.println(e);
            }
        } // end loop openFile

        for (int f = 0; f < factID; f++) {
            numberOfLot.add(0);
        }

        //---------------------------  Ask Input from User -----------------------
        while (matAdd == 0) {
            try {
                program.printThreadName();
                System.out.printf(" >> Enter amount of material per day\n");
                matAdd = Integer.parseInt(scanInput.next());
            } catch (RuntimeException e) {
                System.err.println("Invalid input. \n" + e);
            }System.out.printf("-------------------------------------------------------------------------------\n");
        } // end read material per day input

        while (days == 0) {
            try {
                program.printThreadName();
                System.out.printf(" >> Enter number of days\n");
                days = Integer.parseInt(scanInput.next());
            } catch (RuntimeException e) {
                System.err.println("Invalid input. \n" + e);
            }
            
            System.out.printf("-------------------------------------------------------------------------------\n");
        } // end read days input

        
        //----------------------  Print Requirement of Each factory ----------------------
        System.out.println("                               * Information *\n");
        for (int f = 0; f < facRequired.size(); f++) { // Each Factory, facRequired.size() = number of Factories
            program.printThreadName();
            System.out.printf(" >> %-8s factory  |   %-3d units per lot  |  materials per lot = ", prodName.get(f),
                    upl.get(f));
            for (int m = 0; m < facRequired.get(f).size(); m++) { // Each material Required, facRequired.get(i).size() = number of materials type
                System.out.printf("%3d %s",upl.get(f)* facRequired.get(f).get(m), matName.get(m)); // facRequired.get(i).get(m) = amount of required materials
                if (m == facRequired.get(f).size() - 1) {
                    System.out.printf("\n");
                } // If m = last material -> new line
                else {
                    System.out.printf(", ");
                }
            }
        }
        
        //---------------------------  Print Day & Put material --------------------------
        for (int d = 0; d < days; d++) {
            ArrayList<Factory> factory = new ArrayList<Factory>(); //Runnable Object
            System.out.printf("\n===============================================================================\n");
            program.printThreadName();
            System.out.printf(" >> Day %d\n", d + 1);

            // Put Material
            for (int i = 0; i < matName.size(); i++) {
                material.add(new OneShareMaterial(matName.get(i), matAdd));
                material.get(i).putMaterial();
                material.get(i).printListMaterial();
            }
            System.out.println();

            
            //--------------------------- Run Thread (Still in day loop) -----------------------
            // Get Material (Thread work)
            ArrayList<Thread> threadToday = new ArrayList<Thread>(); //Keep All factory thread today
            for (int f = 0; f < factID; f++) {
                //factory is Runnable Object (For returning value even if thread dead from join)
                factory.add(new Factory(f, upl.get(f), facRequired.get(f), material, numberOfLot.get(f), facMatLeft.get(f)));
                Thread facThread = new Thread(factory.get(f), prodName.get(f));
                threadToday.add(facThread);
                facThread.start();
            }
            for (int f = 0; f < factID; f++) {
                try {
                    threadToday.get(f).join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                facMatLeft.set(f, factory.get(f).getMaterialLeft()); // Create method in Factory
                numberOfLot.set(f, factory.get(f).getNumberOfLot());
            }
        }//end day
    
        //-------------------------------- Print Summary ---------------------------------
        System.out.printf("\n===============================================================================\n");
        program.printThreadName();
        
        System.out.printf(" >> Summary \n");

        //Create Object for Compare
        ArrayList<LotCompare> summary = new ArrayList<LotCompare>();
        for (int i = 0; i < factID; i++) {
            summary.add(new LotCompare(numberOfLot.get(i),prodName.get(i)));
        }
        Collections.sort(summary);
        for (int i = 0; i < factID; i++) {
            program.printThreadName();
            System.out.printf(" >> Total %-8s Lots = %d\n", summary.get(i).getNameFac(), summary.get(i).getNumLots());
        }
        System.out.printf("===============================================================================\n");
        scanInput.close();
    }// end main
}// end FactorySimulation

class MyUtility {
    public void printThreadName() {
        System.out.printf("Thread %-9s", Thread.currentThread().getName());
    }
}

class LotCompare implements Comparable<LotCompare>{
    private int numLots;
    private String nameFac;

    public LotCompare(int nls, String nf)
    {
      numLots = nls;
      nameFac = nf;
    }

    public int compareTo(LotCompare other)
    {
        if(this.numLots > other.numLots) return -1;
        else if(this.numLots == other.numLots) return 0;
        else return 1;
    }
    
    public int getNumLots(){
        return numLots;
    }

    public String getNameFac(){
        return nameFac;
    }

}