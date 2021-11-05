import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

/*Member 
1. Warunyupa  Lerdsaeng        6313180
2. Nalin      Suesangiamsakul  6313216*/

class Factory extends Thread {
    private int ID, lotSize, countLots;
    private String product;
    private ArrayList<Integer> requiredMaterial;
    private ArrayList<OneShareMaterial> OneShareArray;
    MyUtility program = new MyUtility();

    public Factory() {
    }

    public Factory(int id, String p, int l, ArrayList<Integer> rMaterial, ArrayList<OneShareMaterial> OneShare) {
        super(p);
        ID = id;
        product = p;
        lotSize = l;
        requiredMaterial = rMaterial;
        OneShareArray = OneShare;
        countLots = 0;
    }

    public void run() {
        int tempGet;
        for(int i=0;i<requiredMaterial.size();i++){
            tempGet = OneShareArray.get(i).getMaterial(requiredMaterial.get(i));
            if(tempGet == requiredMaterial.get(i)){
                
            }
        }
    }

}// end Factory

class OneShareMaterial {
    private String name;
    private int balance, supplierPut;
    MyUtility program = new MyUtility();

    public OneShareMaterial() {
    }

    public OneShareMaterial(String n, int b) {
        name = n;
        balance = b;
        supplierPut = b;
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
        return numGet;
    }

    public void printListMaterial() {
        // check all list Materials
        program.printThreadName();
        System.out.printf(" >> Put %,5d %10s \tBalance = %,5d %10s\n", supplierPut, name, balance, name);
    }
}// end OneShareMaterial

class FactorySimulation {
    public static void main(String[] args) {
        MyUtility program = new MyUtility();
        ArrayList<OneShareMaterial> material = new ArrayList<OneShareMaterial>(); // ArrayList is used when we don't
                                                                                  // know exact value
        Boolean openSuccess = false, readLine1 = false;
        Scanner scanInput = new Scanner(System.in);

        String fileName;
        int factID = 0;
        // 2D ArrayList facRequired keep ArrayList of each type of materials required
        // amount;
        ArrayList<ArrayList<Integer>> facRequired = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> matName = new ArrayList<String>();
        ArrayList<String> prodName = new ArrayList<String>();
        ArrayList<Integer> upl = new ArrayList<Integer>();
        // *ArrayList<Integer> numberOfLot = new ArrayList<Integer>();
        int matAdd = 0, days = 0;

        while (openSuccess == false) {
            program.printThreadName();
            System.out.printf(" >> Enter product specification file = \n");
            fileName = scanInput.next();

            try (Scanner scanFile = new Scanner(new File(fileName))) {
                openSuccess = true;
                while (scanFile.hasNext()) { // Read 1 line per round

                    ArrayList<Integer> matRequired = new ArrayList<Integer>();

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
                            // Add Amount of each type of materials requirement and keep in Array
                            // matRequired
                            matRequired.add(Integer.parseInt(buf[i].trim()));
                        }
                        facRequired.add(matRequired); // Example - There are 3 factory; pants shirt jeans, but each
                                                      // required 2 material; buttons, zippers
                    }
                }
            } // end try
            catch (FileNotFoundException e) {
                System.out.println("File missing");
                System.err.println(e);
            } catch (RuntimeException e) {
                System.err.println(e);
            }
        } // end loop openFile

        while (matAdd == 0) {
            try {
                program.printThreadName();
                System.out.printf(" >> Enter amount of material per day\n");
                matAdd = Integer.parseInt(scanInput.next());
            } catch (RuntimeException e) {
                System.err.println("Invalid input. \n" + e);
            }
        } // end read material per day input

        while (days == 0) {
            try {
                program.printThreadName();
                System.out.printf(" >> Enter number of days\n");
                days = Integer.parseInt(scanInput.next());
            } catch (RuntimeException e) {
                System.err.println("Invalid input. \n" + e);
            }
        } // end read days input
        for (int f = 0; f < facRequired.size(); f++) { // Each Factory; facRequired.size() = number of Factories
            program.printThreadName();
            System.out.printf(" >> %-8s factory  |   %-3d units per lot  |  materials per lot = ", prodName.get(f),
                    upl.get(f));
            for (int m = 0; m < facRequired.get(f).size(); m++) { // Each material Required; facRequired.get(i).size() =
                                                                  // number of materials type
                System.out.printf("%3d %s", facRequired.get(f).get(m), matName.get(m)); // facRequired.get(i).get(m) =
                                                                                        // amount of required materials
                if (m == facRequired.get(f).size() - 1) {
                    System.out.printf("\n");
                } // If m = last material -> new line
                else {
                    System.out.printf(", ");
                }
            }
        }

        for (int d = 0; d < days; d++) {
            ArrayList<Factory> factory = new ArrayList<Factory>();
            System.out.println();
            program.printThreadName();
            System.out.printf(" >> Day %d\n", d + 1);
            for (int i = 0; i < matName.size(); i++) {
                material.add(new OneShareMaterial(matName.get(i), matAdd));
                material.get(i).printListMaterial();
            }
            for (int f = 0; f < factID; f++) {
                factory.add(new Factory(f, prodName.get(f), upl.get(f), facRequired.get(f), material));
                factory.get(f).start();

                try {
                    factory.get(f).join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
        scanInput.close();

        program.printThreadName();
        System.out.printf(" >> Summary ");
        program.printThreadName();
        for (int i = 0; i < factID; i++) {
            // *System.out.printf(" Total %-8s Lots = %d", prodName.get(i),
            // numberOfLot.get(i));
        }
    }// end main
}// end FactorySimulation

class MyUtility {
    public void printThreadName() {
        System.out.printf("Thread %-5s", Thread.currentThread().getName());
    }
}