/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Grant Guglielmo
 * gg25488
 * 16470
 * Mohit Joshi
 * msj696
 * 16475
 * Slip days used: 0
 * Fall 2016
 */
package assignment4; // cannot be in default package
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Method;


/*
 * Usage: java assignment4.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    @SuppressWarnings("unused")
	private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        boolean cont = true;
        while(cont){//continue readin inputs until user inputs quit
        	System.out.print("critters>");//display command prompt
        	String commandLine = kb.nextLine();
        	String[] commands = commandLine.split(" ");//split command into array
        	int cycles = 1;
        	switch(commands[0]){//switch to find command
        	case "quit":
        		if(commands.length > 1){//error if any arguments
        			System.out.println("error processing: " + commandLine);
    				break;
        		}
        		cont = false;//exit program
        		break;
        	case "show":
        		if(commands.length > 1){//error if any arguments
        			System.out.println("error processing: " + commandLine);
    				break;
        		}
        		Critter.displayWorld();//display critters in a grid
        		break;
        	case "step":
        		if(commands.length > 2){//error if 3 or more arguments
        			System.out.println("error processing: " + commandLine);
    				break;
        		}
        		if(commands.length > 1){//check if any arguments for number of steps, default to 1
        			try{
        				cycles = Integer.parseInt(commands[1]);
        			}
        			catch(Exception e){//error if not integer
        				System.out.println("error processing: " + commandLine);
        				break;
        			}
        		}
        		for(int i = 0; i < cycles; i++){//loop through specified number of time steps
        			Critter.worldTimeStep();
        		}
        		break;
        	case "seed":
        		if(commands.length != 2){//error if not 1 argument
        			System.out.println("error processing: " + commandLine);
    				break;
        		}
        		try{
        			long seed = Long.parseLong(commands[1]);//seed random number generator
        			Critter.setSeed(seed);
            		break;
        		}
        		catch(Exception e){//error if not integer
    				System.out.println("error processing: " + commandLine);
    				break;
    			}
        	case "make":
        		if(commands.length > 3 || commands.length < 2){//error if not 1-2 arguments
        			System.out.println("error processing: " + commandLine);
    				break;
        		}
        		String className = commands[1];
        		if(commands.length > 2){
        			try{//check if second argument is integer
        				cycles = Integer.parseInt(commands[2]);
        			}
        			catch(Exception e){
        				System.out.println("error processing: " + commandLine);
        				break;
        			}
        		}
        		for(int i = 0; i < cycles; i++){//make specified number of critters of specififed type
        			try {
						Critter.makeCritter(className);
					} catch (InvalidCritterException e) {
						System.out.println("error processing: " + commandLine);
						break;
					}
        		}
        		break;
        	case "stats":
        		if(commands.length != 2){//error if not 1 argument 
        			System.out.println("error processing: " + commandLine);
    				break;
        		}
        		String statName = commands[1];
        		try {//call static method of specified class
					List<Critter> statList = Critter.getInstances(statName);
					Class<?>[] types = {List.class};
					Class<?> testClass = Class.forName(myPackage + "." + statName);
					Method stat = testClass.getMethod("runStats",types);
					stat.invoke(null, statList);
				} catch (Exception e) {
					System.out.println("error processing: " + commandLine);
				}
        		break;
        	default://not a valid command inputed
        		System.out.println("invalid command: " + commandLine);
        	}
        }
        
        /* Write your code above */
        System.out.flush();

    }
}
