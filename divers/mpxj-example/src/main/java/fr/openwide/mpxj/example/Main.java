package fr.openwide.mpxj.example;


public class Main {

	public static void main(String[] args) {
		MSProjectGeneration mspGen = new MSProjectGeneration();
		
		try {
			mspGen.write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
