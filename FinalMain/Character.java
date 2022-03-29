package FinalMain;

import java.util.ArrayList;

public class Character {
	private int _health;
	private int maxHealth;
	private String _race;
	private ArrayList<Item> _inventory = new ArrayList<>();;
	public Item weapon;
	public Item armor;
	private String _name;

	public Character() {
	}

	public Character(String name, String race) {
		set_name(name);
		set_race(race);
		_health = 50;
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

	public ArrayList<Item> get_inventory() {
		return _inventory;
	}

	public void set_inventory(ArrayList<Item> _inventory) {
		this._inventory = _inventory;
	}

	public boolean inventoryContains(int i) {
		return (_inventory.size() > i && i >= 0);
	}

	public Item getItem(int i) {
		if (_inventory.size() > i) {
			return _inventory.get(i);
		}
		return null;
	}

	public void addToInventory(Item newItem) {
		_inventory.add(newItem);
	}

	public Item removeFromInventory(Item i) {
		if (_inventory.contains(i))
			_inventory.remove(i);
		return i;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}
