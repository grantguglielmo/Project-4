/* CRITTERS Critter2.java
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

public class Critter2 extends TestCritter{
	
	public Critter2(){
		
	}

	@Override
	public void doTimeStep() {
		int deciscion = Critter.getRandomInt(42);//randomly decide whether to walk run or rest
		if(deciscion < 21){
			walk(Critter.getRandomInt(8));
		}
		else if(deciscion < 31){
			run(Critter.getRandomInt(8));
		}
		else{
			this.setEnergy(getEnergy() - Params.rest_energy_cost);
		}
	}

	@Override
	public boolean fight(String oponent) {
		while(getEnergy() > Params.min_reproduce_energy){//critter just spams area with babies then kills itself
			Critter2 child = new Critter2();
			this.reproduce(child, Critter.getRandomInt(8));
		}
		return false;
	}
	
}
