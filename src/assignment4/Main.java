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

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.lang.reflect.Method;

/*
 * Usage: java assignment4.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main extends Application{
	
	public static GridPane g;
	public static int scale = (int)Math.max(1,80000/(Params.world_height*Params.world_width));
	static Scanner kb; // scanner connected to keyboard input, or input file
	private static String inputFile; // input file, used instead of keyboard
										// input if specified
	static ByteArrayOutputStream testOutputString; // if test specified, holds
													// all console output
	private static String myPackage; // package of Critter file. Critter cannot
										// be in default pkg.
	protected static boolean DEBUG = false; // Use it or not, as you wish!
	static PrintStream old = System.out; // if you want to restore output to
											// console

	// Gets the package name. The usage assumes that Critter and its subclasses
	// are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            args can be empty. If not empty, provide two parameters -- the
	 *            first is a file name, and the second is test (for test output,
	 *            where all output to be directed to a String), or nothing.
	 */
	public static void main(String[] args) {
		if (DEBUG) {
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
					if (args[1].equals("test")) { // if the word "test" is the
													// second argument to java
						// Create a stream to hold the output
						testOutputString = new ByteArrayOutputStream();
						PrintStream ps = new PrintStream(testOutputString);
						// Save the old System.out.
						old = System.out;
						// Tell Java to use the special stream; all console
						// output will be redirected here from now
						System.setOut(ps);
					}
				}
			} else { // if no arguments to main
				kb = new Scanner(System.in); // use keyboard and console
			}

			/* Do not alter the code above for your submission. */
			/* Write your code below. */
			boolean cont = true;
			while (cont) {// continue readin inputs until user inputs quit
				System.out.print("critters>");// display command prompt
				String commandLine = kb.nextLine();
				String[] commands = commandLine.split(" ");// split command into
															// array
				int cycles = 1;
				switch (commands[0]) {// switch to find command
				case "quit":
					if (commands.length > 1) {// error if any arguments
						System.out.println("error processing: " + commandLine);
						break;
					}
					cont = false;// exit program
					break;
				case "show":
					if (commands.length > 1) {// error if any arguments
						System.out.println("error processing: " + commandLine);
						break;
					}
					Critter.displayWorld();// display critters in a grid
					break;
				case "step":
					if (commands.length > 2) {// error if 3 or more arguments
						System.out.println("error processing: " + commandLine);
						break;
					}
					if (commands.length > 1) {// check if any arguments for
												// number of steps, default to 1
						try {
							cycles = Integer.parseInt(commands[1]);
						} catch (Exception e) {// error if not integer
							System.out.println("error processing: " + commandLine);
							break;
						}
					}
					for (int i = 0; i < cycles; i++) {// loop through specified
														// number of time steps
						Critter.worldTimeStep();
					}
					break;
				case "seed":
					if (commands.length != 2) {// error if not 1 argument
						System.out.println("error processing: " + commandLine);
						break;
					}
					try {
						long seed = Long.parseLong(commands[1]);// seed random
																// number
																// generator
						Critter.setSeed(seed);
						break;
					} catch (Exception e) {// error if not integer
						System.out.println("error processing: " + commandLine);
						break;
					}
				case "make":
					if (commands.length > 3 || commands.length < 2) {// error if
																		// not
																		// 1-2
																		// arguments
						System.out.println("error processing: " + commandLine);
						break;
					}
					String className = commands[1];
					if (commands.length > 2) {
						try {// check if second argument is integer
							cycles = Integer.parseInt(commands[2]);
						} catch (Exception e) {
							System.out.println("error processing: " + commandLine);
							break;
						}
					}
					for (int i = 0; i < cycles; i++) {// make specified number
														// of critters of
														// specififed type
						try {
							Critter.makeCritter(className);
						} catch (InvalidCritterException e) {
							System.out.println("error processing: " + commandLine);
							break;
						}
					}
					break;
				case "stats":
					if (commands.length != 2) {// error if not 1 argument
						System.out.println("error processing: " + commandLine);
						break;
					}
					String statName = commands[1];
					try {// call static method of specified class
						List<Critter> statList = Critter.getInstances(statName);
						Class<?>[] types = { List.class };
						Class<?> testClass = Class.forName(myPackage + "." + statName);
						Method stat = testClass.getMethod("runStats", types);
						stat.invoke(null, statList);
					} catch (Exception e) {
						System.out.println("error processing: " + commandLine);
					}
					break;
				default:// not a valid command inputed
					System.out.println("invalid command: " + commandLine);
				}
			}

			/* Write your code above */
			System.out.flush();

		}
		else{
			launch(args);
		}
	}
	
	public static void write(Critter c, int x, int y){
		Shape shape;
		switch(c.viewShape()){
		case CIRCLE:
			shape = new Circle(scale / 2);
			shape.setFill(c.viewFillColor());
			break;
		case SQUARE:
			shape = new Rectangle(scale, scale);
		case TRIANGLE:
			
		case DIAMOND:
		case STAR:
		default:
			shape = new Circle();
		}
		g.add(shape, x, y);
}

	@Override
	public void start(Stage primaryStage) throws Exception {
		int x_val = 400;
		int y_val = 900;
		primaryStage.setTitle("Critter World");
		GridPane grid = new GridPane();
		primaryStage.setX(x_val +42);
		primaryStage.setY(0);
		for(int i = 0; i < Params.world_width; i++) {
            ColumnConstraints column = new ColumnConstraints(scale);
            grid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < Params.world_height; i++) {
            RowConstraints row = new RowConstraints(scale);
            grid.getRowConstraints().add(row);
        }
        g = grid;
		Critter.displayWorld();
		Scene s = new Scene(grid, Params.world_width*scale, Params.world_height*scale, Color.WHITE);
		primaryStage.setScene(s);
		primaryStage.show();
		
		GridPane root = new GridPane();
		Stage control = new Stage();
		control.setTitle("Controller");
		Scene control_scene = new Scene(root, x_val, y_val, Color.WHITE);
		control.setScene(control_scene);
		control.setX(0);
		control.setY(0);
		
		ColumnConstraints column = new ColumnConstraints(x_val/3);
        root.getColumnConstraints().add(column);
        RowConstraints row = new RowConstraints(y_val/9);
        root.getRowConstraints().add(row);
        root.getRowConstraints().add(row);
        Button btn0 = new Button();
        btn0.setText("make");
        Button btn1 = new Button();
        btn1.setText("step");
        root.add(btn0, 1, 1);
        root.add(btn1, 1, 2);
        btn0.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
                try {
					Critter.makeCritter("Algae");
				} catch (InvalidCritterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        btn1.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
                Critter.worldTimeStep();
                Critter.displayWorld();
                primaryStage.show();
            }
        });
		control.show();
	}
}
