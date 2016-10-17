/* CRITTERS Critter1.java
 * EE422C Project 4 submission by
 * Grant Guglielmo
 * gg25488
 * 16470
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: 0
 * Fall 2016
 */
package assignment4;

import assignment4.Critter.TestCritter;

public class Critter1 extends TestCritter{
	
	private int[] dir;
	
	public Critter1(){
		dir = new int[3];
		dir[0] = Critter.getRandomInt(8);
		int x = Critter.getRandomInt(8);
		while(x == dir[0]){
			x = Critter.getRandomInt(8);
		}
		dir[1] = x;
		x = Critter.getRandomInt(8);
		while(x == dir[0] || x == dir[1]){
			x = Critter.getRandomInt(8);
		}
		dir[2] = x;
	}
	
	@Override
	public void doTimeStep() {
		run(dir[Critter.getRandomInt(3)]);
		if(this.getEnergy() >= 5*Params.min_reproduce_energy){
			Critter1 child = new Critter1();
			for(int i = 0; i < 3; i++){
				int x = Critter.getRandomInt(3);
				child.dir[x] = this.dir[x];
			}
			reproduce(child, child.dir[0]);
		}
	}

	@Override
	public boolean fight(String oponent) {
		return true;
	}
	
}
