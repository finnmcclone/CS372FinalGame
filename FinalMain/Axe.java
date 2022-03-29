package FinalMain;

import java.util.Random;

public class Axe extends Weapon {

	String[] weaponAdjectives = { "wrathful", "vengeful", "burning", "freezing", "weak", "scrawny", "powerful",
			"strong", "average", "pretty normal", "colorful", "smelly" };
	String[] weaponTraits = { "harming", "burning", "freezing", "charming", "damaging", "brawling", "defense", "death",
			"poison", "disease", "meat", "steel" };
	String str = "";

	Random rand = new Random();

	public Axe(int level, String name) {
		super(level, name);
		String adj = weaponAdjectives[rand.nextInt(weaponAdjectives.length)];
		String trait = weaponTraits[rand.nextInt(weaponTraits.length)];
		str = adj + " axe  of " + trait + ", level: " + level;
	}

	public String toString() {
		return str;
	}

}
