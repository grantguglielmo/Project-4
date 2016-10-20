/* CRITTERS Critter.java
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
package assignment4;

import java.util.*;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */

public abstract class Critter {
	private static String myPackage;
	private static List<Critter> population = new java.util.ArrayList<Critter>();//arraylist to hold all living critters
	private static List<Critter> babies = new java.util.ArrayList<Critter>();//array for babies that havent been added to population yet
	private static boolean stepOver;//global flag

	// Gets the package name. This assumes that Critter and its subclasses are
	// all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	private static java.util.Random rand = new java.util.Random();

	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}

	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}

	/*
	 * a one-character long string that visually depicts your critter in the
	 * ASCII interface
	 */
	public String toString() {
		return "";
	}

	private int energy = 0;

	protected int getEnergy() {
		return energy;
	}

	private int x_coord;
	private int y_coord;
	private boolean moved;
	/**
	 * move critter in specififed direction, wont move critter if it has already moved this time step.
	 * If moved is called during the encounters stage of the time step then critters can not moved to occupied locations
	 * @param direction direction for critter to walk in    directions: 3 2 1
	 * 																	4 * 0
	 * 																	5 6 7
	 */
	protected final void walk(int direction) {
		if (!moved) {//check if critter has already moved
			int oldX = 0;
			int oldY = 0;
			if (stepOver) {//check if encounters stage
				oldX = this.x_coord;//save locations before movement
				oldY = this.y_coord;
			}
			switch (direction) {// move critter based on direction
			case 0: //grid is a torus so critters will wrap around
				this.x_coord = (this.x_coord + 1) % Params.world_width;
				break;
			case 1:
				this.x_coord = (this.x_coord + 1) % Params.world_width;
				this.y_coord--;
				if (this.y_coord < 0) {
					this.y_coord = Params.world_height - 1;
				}
				break;
			case 2:
				this.y_coord--;
				if (this.y_coord < 0) {
					this.y_coord = Params.world_height - 1;
				}
				break;
			case 3:
				this.x_coord--;
				if (this.x_coord < 0) {
					this.x_coord = Params.world_width - 1;
				}
				this.y_coord--;
				if (this.y_coord < 0) {
					this.y_coord = Params.world_height - 1;
				}
				break;
			case 4:
				this.x_coord--;
				if (this.x_coord < 0) {
					this.x_coord = Params.world_width - 1;
				}
				break;
			case 5:
				this.x_coord--;
				if (this.x_coord < 0) {
					this.x_coord = Params.world_width - 1;
				}
				this.y_coord = (this.y_coord + 1) % Params.world_height;
				break;
			case 6:
				this.y_coord = (this.y_coord + 1) % Params.world_height;
				break;
			case 7:
				this.x_coord = (this.x_coord + 1) % Params.world_width;
				this.y_coord = (this.y_coord + 1) % Params.world_height;
				break;
			}
			if(stepOver){//if encounters stage check if new location is occupied
				for(Critter c : population){//if it is occupied revert critters location
					if(this != c){
						if(this.x_coord == c.x_coord && this.y_coord == c.y_coord && c.energy > 0){
							this.x_coord = oldX;
							this.y_coord = oldY;
							break;
						}
					}
				}
			}
		}
		moved = true;//mark critter as having moved
		this.energy -= Params.walk_energy_cost;//decrease critters energy no matter what
	}
	/**
	 * same method as walk except it moves critters 2 space. allows critters to run "past" other critters during encounters stage
	 * @param direction direction for critter to run in
	 */
	protected final void run(int direction) {
		if(stepOver){//check if encounters stage
			int oldX = this.x_coord;
			int oldY = this.y_coord;
			stepOver = false;//critter can run past other critters so allow critter overlap during first walk
			this.walk(direction);// move critter in given direction twice
			stepOver = true;
			int medX = this.x_coord;//dont allow critter overlap during second walk
			int medY = this.y_coord;
			moved = false;
			this.walk(direction);
			if(this.x_coord == medX && this.y_coord == medY){//check if critter moved 2 spaces away, revert to original if it didnt move
				this.x_coord = oldX;
				this.y_coord = oldY;
			}
			this.energy -= (Params.run_energy_cost - (2 * Params.walk_energy_cost));//subtract run energy
			return;
		}
		this.walk(direction);// move critter in given direction twice
		moved = false;//unmark critter as having moved so it can walk again, will be marked moved after next walk
		this.walk(direction);
		this.energy -= (Params.run_energy_cost - (2 * Params.walk_energy_cost));//add back walk energy then subtract run energy
	}
	/**
	 * place baby critter is specified place from the parent, and correct baby and parents energy.
	 * add baby to babies array to be added to population latter
	 * @param offspring child of parent
	 * @param direction direction to be placed at
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (this.energy < Params.min_reproduce_energy) {//reproduce fail if parent lacks energy
			return;
		}
		int lowerHalf = this.energy / 2;//set parent and childs energy to half of the parents, round up for parent down for child
		offspring.energy = lowerHalf + Params.walk_energy_cost;
		offspring.moved = false;
		this.energy = this.energy - lowerHalf;
		boolean hold = stepOver;//if encounters stage allow baby to occupy same position as another critter
		stepOver = false;
		offspring.walk(direction);//move baby in direction
		stepOver = hold;
		babies.add(offspring);//add baby to array
	}

	public abstract void doTimeStep();

	public abstract boolean fight(String oponent);

	/**
	 * create and initialize a Critter subclass. critter_class_name must be the
	 * unqualified name of a concrete subclass of Critter, if not, an
	 * InvalidCritterException must be thrown. (Java weirdness: Exception
	 * throwing does not work properly if the parameter has lower-case instead
	 * of upper. For example, if craig is supplied instead of Craig, an error is
	 * thrown instead of an Exception.)
	 * 
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Critter makeCritter;
		try {
			Class<?> testClass = Class.forName(myPackage + "." + critter_class_name);// sets
			// testClass to
			// critter_class_name
			makeCritter = (Critter) testClass.newInstance();// runs default
															// constructor for
															// testClass
		} catch (Exception e) {// catch any errors and throw invalid critter
								// exception
			throw new InvalidCritterException(critter_class_name);
		}
		makeCritter.x_coord = getRandomInt(Params.world_width);//give critter random starting location
		makeCritter.y_coord = getRandomInt(Params.world_height);
		makeCritter.energy = Params.start_energy;
		makeCritter.moved = false;
		population.add(makeCritter);//add critter to population
	}

	/**
	 * Gets a list of critters of a specific type.
	 * 
	 * @param critter_class_name
	 *            What kind of Critter is to be listed. Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		for (Critter c : population) {
			try {
				if (c.getClass().equals(Class.forName(myPackage + "." + critter_class_name))) {//find all critters of specified class
					result.add(c);
				}
			} catch (Exception e) {
				throw new InvalidCritterException(critter_class_name);
			}
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * 
	 * @param critters
	 *            List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string, 1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();
	}

	/*
	 * the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this
	 * class and then use the setter functions contained here.
	 * 
	 * NOTE: you must make sure that the setter functions work with your
	 * implementation of Critter. That means, if you're recording the positions
	 * of your critters using some sort of external grid or some other data
	 * structure in addition to the x_coord and y_coord functions, then you MUST
	 * update these setter functions so that they correctly update your
	 * grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}

		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}

		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}

		protected int getX_coord() {
			return super.x_coord;
		}

		protected int getY_coord() {
			return super.y_coord;
		}

		/*
		 * This method getPopulation has to be modified by you if you are not
		 * using the population ArrayList that has been provided in the starter
		 * code. In any case, it has to be implemented for grading tests to
		 * work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using
		 * the babies ArrayList that has been provided in the starter code. In
		 * any case, it has to be implemented for grading tests to work. Babies
		 * should be added to the general population at either the beginning OR
		 * the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {//clear all arrays
		population.clear();
		babies.clear();
	}

	public static void worldTimeStep() {
		EncounterList collisions = new EncounterList();//create a new collisions array
		int[][] location = new int[Params.world_height][Params.world_width];//create a new matrix that stores critters locations
		stepOver = false;
		for (int i = 0; i < population.size(); i++) {
			Critter c = population.get(i);
			c.doTimeStep();//call time stpe for all critters
			c.energy -= Params.rest_energy_cost;//subtract rest energy
			int indexCheck = location[c.y_coord][c.x_coord];// checks if c just
															// moved into a
															// position occupied
															// by a critter
			if (indexCheck != 0) { // that has already initiated doTimeStep
				if (indexCheck == -1) { // adds the 2 critters into a list
					indexCheck = 0;
				}
				collisions.add(c.x_coord, c.y_coord, population.get(indexCheck), c);
			} else {// records index of c into location in matrix
				int j = i;
				if (j == 0) {
					j = -1;
				}
				location[c.y_coord][c.x_coord] = j;
			}
		}
		stepOver = true;//encounters stage
		for (int i = 0; i < Params.world_height * Params.world_width; i++) {// resolve
																			// encounters
			ArrayList<Critter> currentSpot = collisions.encounters.get(i);//take the encounter list for the current location
			if (currentSpot != null) {//check if the list is not null
				while (currentSpot.size() > 1) {//while their are at least 2 bugs at the current location
					Critter a = currentSpot.get(0);
					Critter b = currentSpot.get(1);
					int oldX = a.x_coord;//store their orignal locations
					int oldY = a.y_coord;
					boolean aFight = a.fight(b.toString());//call fight for both critters
					boolean bFight = b.fight(a.toString());
					if (a.energy <= 0) {//check if either of the bugs is dead, in which case ignore them
						currentSpot.remove(0);
						if (b.energy <= 0) {
							currentSpot.remove(0);
						}

					} else if (b.energy <= 0) {
						currentSpot.remove(1);
					} else if ((bFight && aFight) || (a.x_coord == b.x_coord && a.y_coord == b.y_coord)) {//see if bothe the bugs want to fight
						int aRoll = Critter.getRandomInt(a.energy);//or if they are still in the same location
						if (!aFight) {//if the bugs dont want to fight set their roll to zero
							aRoll = 0;
						}
						int bRoll = Critter.getRandomInt(a.energy);
						if (!bFight) {
							bRoll = 0;
						}
						if (aRoll >= bRoll) {//checkwho scored higher on the roll
							if(a.toString().equals("@")){//algae always looses
								b.energy += a.energy / 2;
								a.energy = 0;
								currentSpot.remove(0);
							}
							else{
							a.energy += b.energy / 2;
							b.energy = 0;
							currentSpot.remove(1);
							}
						} else {
							b.energy += a.energy / 2;
							a.energy = 0;
							currentSpot.remove(0);
						}
					} else if (!aFight) {//figure out which bug ran away
						if (bFight) {
							currentSpot.remove(0);
						} else if (a.x_coord != oldX || a.y_coord != oldY) {
							currentSpot.remove(0);
							if(b.x_coord != oldX || b.y_coord != oldY){
								currentSpot.remove(0);
							}
						} else {
							currentSpot.remove(1);
						}
					} else {
						currentSpot.remove(1);
					}
				}
			}
		}
		stepOver = false;//encounter stage over
		kill();//remove dead bugs from population
		for (Critter babe : babies) {
			population.add(babe);//add babies to population
		}
		babies.clear();
		for (int i = 0; i < Params.refresh_algae_count; i++) {//add algae to population
			try {
				makeCritter("Algae");
			} catch (InvalidCritterException e) {
				e.printStackTrace();
			}
		}
		for (Critter c : population) {//marked bugs as not moved for next time step
			c.moved = false;
		}
	}
	/**
	 * remove all dead bugs from population
	 */
	private static void kill() {
		for (int i = 0; i < population.size(); i++) {//remove any bug with energy <= 0
			if (population.get(i).energy < 1) {
				population.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * displays the torus world that the critters live in by representing
	 * it as a grid. display each critter as their toString().
	 */
	public static void displayWorld() {
		int[][] indexMatrix = new int[Params.world_height][Params.world_width];
		if (population.size() > 0) {
			indexMatrix[population.get(0).y_coord][population.get(0).x_coord] = -1;//create matrix with every critter stored
		}
		for (int i = 1; i < population.size(); i++) {
			indexMatrix[population.get(i).y_coord][population.get(i).x_coord] = i;//as their index, index 0 is -1 though
		}
		System.out.print("\n+");
		for (int i = 0; i < Params.world_width; i++) {//display grid using matrix above
			System.out.print("-");
		}
		System.out.print("+\n");
		for (int i = 0; i < Params.world_height; i++) {
			System.out.print("|");
			for (int j = 0; j < Params.world_width; j++) {
				if (indexMatrix[i][j] == 0) {
					System.out.print(" ");
				} else if (indexMatrix[i][j] == -1) {
					System.out.print(population.get(0).toString());
				} else {
					System.out.print(population.get(indexMatrix[i][j]).toString());//call toString() to get representation of the critter
				}
			}
			System.out.print("|\n");
		}
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.print("+\n");
	}
}
