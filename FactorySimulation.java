import java.io.*;
import java.util.Scanner;

/*Member 
1. Warunyupa  Lerdsaeng        6313180
2. Nalin      Suesangiamsakul  6313216*/

class FactorySimulation {
    public static void main(String[] args) {
        FactorySimulation program = new FactorySimulation();

        Boolean openSuccess = false;
        Scanner scanInput = new Scanner(System.in);

        String fileName;
        int factID, upl, mr1, mr2;
        String prd;

        while (openSuccess == false) {
            program.printThreadName();
            System.out.printf(" >> Enter product specification file = \n");
            fileName = scanInput.next();

            try (Scanner scanFile = new Scanner(new File(fileName))) {
                openSuccess = true;
                while (scanFile.hasNext()) { // Read 1 line per round
                    String line = scanFile.nextLine();
                    String[] buf = line.split(",");
                        factID = Integer.parseInt(buf[0].trim());
                        prd = buf[1];
                        upl = Integer.parseInt(buf[2].trim());
                        mr1 = Integer.parseInt(buf[3].trim());
                        mr2 = Integer.parseInt(buf[4].trim());
                        System.out.println(factID + prd + upl + mr1 + mr2);
                }
            }//end loop scanfile
            catch (FileNotFoundException e) {
                System.err.println(e);
                System.out.println("Please try again.");
            }
            catch (RuntimeException e) {
                System.err.println(e);
            }
        }//end loop open
        scanInput.close();
    }//end main

    public void printThreadName() {
        System.out.printf("Thread %5s", Thread.currentThread().getName());
    }
}//end FactorySimulation

class OneShareMaterial{
    private String name;
    private int balance;
    public OneShareMaterial(){}
    public OneShareMaterial(String n,int b){
        name = n;
        balance = b;
    }
    public String getNameMaterial(){
        //ask Name Material
        return name;
    }
    public int getBalance(){
        //ask Balance Material
        return balance;
    }
    public void putMaterial(int num){
        //suplier add Material
        balance = num;
    }
    synchronized public int getMaterial(int num){
        //factory get Material
        int numGet;

        if(balance - num >=0){
            numGet = balance - num;
        }
        else numGet = 0;
        return numGet;
    }
    public void printListMaterial(){
        //check all list Materials
        System.out.printf("Material : %s Balance : %4d\n",name,balance);
    }
}