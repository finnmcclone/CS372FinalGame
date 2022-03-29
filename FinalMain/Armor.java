package FinalMain;

import java.util.Random;

public class Armor extends Item {

	String str = "";
	String[] adj = { "Leather", "Chain Mail", "Plate", "Iron", "Steel", "Shiny", "spiky", "thorny", "ugly", "Gold",
			"Diamond", "Cobalt" };
	Random rand = new Random();

	public Armor(int level, String name) {
		super(level, name);
		String a = adj[rand.nextInt(adj.length)];
		str += a + " armor, level: " + level;
	}

	public String toString() {
		return str;
	}

}
