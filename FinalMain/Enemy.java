package FinalMain;

import java.awt.Image;

public class Enemy extends MapObject {
	private int _health;
	private int maxHealth;
	private String _race;
	Image img;

	public Enemy() {
	}

	public Enemy(String race, Image i, int h) {
		set_race(race);
		img = i;
		_health = h;
		maxHealth = _health;
	}

	public int get_health() {
		return _health;
	}

	public void set_health(int _health) {
		this._health = _health;
	}

	public void add_health(int h) {
		_health += h;
	}

	public void sub_health(int h) {
		_health -= h;
	}

	public String get_race() {
		return _race;
	}

	public void set_race(String _race) {
		this._race = _race;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}
