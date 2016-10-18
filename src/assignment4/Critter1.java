/* CRITTERS Critter1.java
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


public class Critter1 extends Critter{
	
	private int[] dir;
	
	public String toString(){
		return "$";
	}
	
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
		if(this.getEnergy() >= 5*Params.min_reproduce_energy + Params.start_energy){
			Critter1 child = new Critter1();
			for(int i = 0; i < 3; i++){
				int x = Critter.getRandomInt(3);
				child.dir[x] = this.dir[x];
			}
			this.reproduce(child, child.dir[0]);
		}
	}

	@Override
	public boolean fight(String oponent) {
		return true;//never back down
	}
	
}
