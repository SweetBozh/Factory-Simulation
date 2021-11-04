import java.io.*;
import java.util.Scanner;

/*Member 
1. Warunyupa  Lerdsaeng        6313180
2. Nalin      Suesangiamsakul  6313216*/
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

class FactorySimulation {
    public static void main(String[] args) {
        MyUtility program = new MyUtility();

        Boolean openSuccess = false, readLine1 = false;
        Scanner scanInput = new Scanner(System.in);

        String fileName;
        int factID, upl, mr1, mr2;
        String prd, mat1, mat2;
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
                        mat1 = buf[0];
                        mat2 = buf[1];
                        readLine1 = true;
                    } else {
                        factID = Integer.parseInt(buf[0].trim());
                        prd = buf[1];
                        upl = Integer.parseInt(buf[2].trim());
                        mr1 = Integer.parseInt(buf[3].trim());
                        mr2 = Integer.parseInt(buf[4].trim());
                        System.out.println(factID + prd + upl + mr1 + mr2);
                        // *Constructor Add these data for material usage and print
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

        //*Wait for thread code
        /*for(int i=0; i<days; i++){
            program.printThreadName();
            System.out.printf(" >> Day %d\n", i);

        }*/
        scanInput.close();
    }// end main    
}// end FactorySimulation

class MyUtility{
    public void printThreadName() {
        System.out.printf("Thread %-5s", Thread.currentThread().getName());
    }
}