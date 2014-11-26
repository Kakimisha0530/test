package aicha;

import java.io.IOException;
import java.util.Scanner;

public class Radio {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("LISTE DES STATIONS RADIO ");
		System.out.println("Choississez une station : ");
		for(Stations s: Stations.values())
			System.out.println(s.ordinal() + ". " + s.name());
		
		int num = saisie(sc);
		if(num > Stations.values().length || num < 0)
			num = 0;
		Stations sta = Stations.values()[num];
		try {
			Client client = new Client(sta);
			client.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int saisie(Scanner entree) {
		String saisie = "";
		while (!Utils.isInteger(saisie = entree.nextLine()))
			System.out.println("Cette entree n'est pas valide. Veuillez recommencer : ");
		
		return Integer.parseInt(saisie);
	}
}
