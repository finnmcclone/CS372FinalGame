package FinalMain;

public class Item {
	private int _level;
	private String _name;
	
	public Item(int level, String name) {
		set_level(level);
		set_name(name);
	}

	public int get_level() {
		return _level;
	}

	public void set_level(int _level) {
		this._level = _level;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}
	
	public String toString() {
		return _name + ", level " + _level;
	}
}
