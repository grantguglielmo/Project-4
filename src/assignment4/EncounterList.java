package assignment4;

import java.util.ArrayList;

public class EncounterList {
	public ArrayList<ArrayList<Critter>> encounters;
	public static ArrayList<ArrayList<Critter>> cleanList;
	
	public EncounterList(){
		encounters = new ArrayList<ArrayList<Critter>>();
		for(int i = 0; i < Params.world_height*Params.world_width; i++){
			encounters.add(null);
		}
	}
	
	public static void init(){
		cleanList = new ArrayList<ArrayList<Critter>>();
		for(int i = 0; i < Params.world_height*Params.world_width; i++){
			cleanList.add(null);
		}
	}
	
	public void add(int x, int y, Critter a, Critter b){
		int index = x + y*Params.world_width;
		if(encounters.get(index) == null){
			ArrayList<Critter> newCollision = new ArrayList<Critter>();
			newCollision.add(a);
			newCollision.add(b);
			encounters.set(index, newCollision);
		}
		else{
			ArrayList<Critter> addCollision = encounters.get(index);
			addCollision.add(b);
			encounters.set(index, addCollision);
		}
	}
}
