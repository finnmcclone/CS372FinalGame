/*
 * Final Project for CS372 by Finn McClone
 * Java Swing based RPG
 */

package FinalMain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class FinalMain extends JComponent implements KeyListener, Runnable, MouseListener {

	static JFrame frame;

	// phases for paint
	boolean setClass = false, setName = true, setRace = false, adventure = false, combat = false, combatAttack = false,
			combatDefense = false, combatTransition = false, inventory = false, reward = false;
	final int menuY = 200;

	// variable for thread
	boolean flash = true;

	// character creation variables
	int selectX = 90, selectY = 150;
	int cursorX = 100;
	int taskbar = 30;
	int stage = 1;
	static ArrayList<String> menuChoices = new ArrayList<>();
	int menuSelect = 0;
	String name = "", race;

	Random rand = new Random();
	Timer timer = new Timer();

	// map variables
	MapObject[][] map = new MapObject[16][16];
	Character mainCharacter;
	Image character, background, obstacle1, obstacle2, obstacle3, obstacle4, obstacle5, obstacle6, enemy1, enemy2,
			enemy3, axe, sword, bow, armor, chest, rewardBack, boss, combatBack, inventoryBack, healthPotion;
	int chunkSizeX, chunkSizeY;
	int charX = 0, charY = 0;
	int charI = 0, charJ = 0;

	// combat variables
	double interval; // timer count-down
	int circleSize = 50;
	Point heroPoint = new Point(50, 600), enemyPoint = new Point(800, 50);
	Enemy curEnemy;
	Point enemyAttackBar;
	ArrayList<Point> circles = new ArrayList<>();
	ArrayList<Point> mouseClicks = new ArrayList<>();
	ArrayList<Point> circlesHit = new ArrayList<>();

	// inventory variables
	int menuI = 0, menuJ = 0;
	Item rewardItem;

	public static void main(String[] args) {
		// most work has to be done through the constructor since repaint can't be
		// called in static methods
		FinalMain m = new FinalMain();
	}

	public FinalMain() {
		frame = new JFrame("Game Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - taskbar);
		chunkSizeX = (((int) frame.getWidth()) / 16);
		chunkSizeY = (((int) frame.getHeight()) / 16);

		frame.add(this);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		Thread t = new Thread(this);
		t.start();

		frame.setVisible(true);
	}

	public void initCharacter() {
		adventure = true;
		Toolkit tk = Toolkit.getDefaultToolkit();

		// initialize all of the images used in program
		String locChar = "";
		if (mainCharacter instanceof Knight) {
			locChar = "/resources/Knight.png";
		} else if (mainCharacter instanceof Wizard) {
			locChar = "/resources/Mage.png";
		} else if (mainCharacter instanceof Ranger) {
			locChar = "/resources/Ranger.png";
		}
		URL urlChar = getClass().getResource(locChar);
		character = tk.getImage(urlChar);
		character = character.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

		String locBack = "/resources/Background.png";
		URL urlBack = getClass().getResource(locBack);
		background = tk.getImage(urlBack);
		background = background.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

		String locRewardBack = "/resources/RewardBackground.png";
		URL urlRewardBack = getClass().getResource(locRewardBack);
		rewardBack = tk.getImage(urlRewardBack);
		rewardBack = rewardBack.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

		String locCombatBack = "/resources/CombatBackground.png";
		URL urlCombatBack = getClass().getResource(locCombatBack);
		combatBack = tk.getImage(urlCombatBack);
		combatBack = combatBack.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

		String locinventoryBack = "/resources/InventoryBackground.png";
		URL urlinventoryBack = getClass().getResource(locinventoryBack);
		inventoryBack = tk.getImage(urlinventoryBack);
		inventoryBack = inventoryBack.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

		String locAxe = "/resources/Axe.png";
		URL urlAxe = getClass().getResource(locAxe);
		axe = tk.getImage(urlAxe);
		axe = axe.getScaledInstance(45, 45, Image.SCALE_SMOOTH);

		String locBow = "/resources/Bow.png";
		URL urlBow = getClass().getResource(locBow);
		bow = tk.getImage(urlBow);
		bow = bow.getScaledInstance(45, 45, Image.SCALE_SMOOTH);

		String locArmor = "/resources/Armor.png";
		URL urlArmor = getClass().getResource(locArmor);
		armor = tk.getImage(urlArmor);
		armor = armor.getScaledInstance(45, 45, Image.SCALE_SMOOTH);

		String locSword = "/resources/Sword.png";
		URL urlSword = getClass().getResource(locSword);
		sword = tk.getImage(urlSword);
		sword = sword.getScaledInstance(45, 45, Image.SCALE_SMOOTH);

		String locObstacle1 = "/resources/Obstacle1.png";
		URL urlObstacle1 = getClass().getResource(locObstacle1);
		obstacle1 = tk.getImage(urlObstacle1);
		obstacle1 = obstacle1.getScaledInstance(frame.getWidth() / 16, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		String locChest = "/resources/Chest.png";
		URL urlChest = getClass().getResource(locChest);
		chest = tk.getImage(urlChest);
		chest = chest.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

		String locPotion = "/resources/HealthPotion.png";
		URL urlPotion = getClass().getResource(locPotion);
		healthPotion = tk.getImage(urlPotion);
		healthPotion = healthPotion.getScaledInstance(30, 30, Image.SCALE_SMOOTH);

		String locObstacle3 = "/resources/Obstacle3.png";
		URL urlObstacle3 = getClass().getResource(locObstacle3);
		obstacle3 = tk.getImage(urlObstacle3);
		obstacle3 = obstacle3.getScaledInstance(frame.getWidth() / 16, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		String locObstacle2 = "/resources/Obstacle2.png";
		URL urlObstacle2 = getClass().getResource(locObstacle2);
		obstacle2 = tk.getImage(urlObstacle2);
		obstacle2 = obstacle2.getScaledInstance(frame.getWidth() / 16, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		String locObstacle4 = "/resources/Obstacle4.png";
		URL urlObstacle4 = getClass().getResource(locObstacle4);
		obstacle4 = tk.getImage(urlObstacle4);
		obstacle4 = obstacle4.getScaledInstance(frame.getWidth() / 16, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		String locObstacle5 = "/resources/Obstacle5.png";
		URL urlObstacle5 = getClass().getResource(locObstacle5);
		obstacle5 = tk.getImage(urlObstacle5);
		obstacle5 = obstacle5.getScaledInstance(frame.getWidth() / 16, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		String locObstacle6 = "/resources/Obstacle6.png";
		URL urlObstacle6 = getClass().getResource(locObstacle6);
		obstacle6 = tk.getImage(urlObstacle6);
		obstacle6 = obstacle6.getScaledInstance(frame.getWidth() / 16, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		String locEnemy1 = "/resources/Enemy1.png";
		URL urlEnemy1 = getClass().getResource(locEnemy1);
		enemy1 = tk.getImage(urlEnemy1);
		enemy1 = enemy1.getScaledInstance(frame.getWidth() / 20, frame.getHeight() / 18, Image.SCALE_SMOOTH);

		String locEnemy2 = "/resources/Dragon.png";
		URL urlEnemy2 = getClass().getResource(locEnemy2);
		enemy2 = tk.getImage(urlEnemy2);
		enemy2 = enemy2.getScaledInstance(frame.getWidth() / 20, frame.getHeight() / 18, Image.SCALE_SMOOTH);

		String locEnemy3 = "/resources/Evil_Knight.png";
		URL urlEnemy3 = getClass().getResource(locEnemy3);
		enemy3 = tk.getImage(urlEnemy3);
		enemy3 = enemy3.getScaledInstance(frame.getWidth() / 20, frame.getHeight() / 18, Image.SCALE_SMOOTH);

		String locBoss = "/resources/Boss.png";
		URL urlBoss = getClass().getResource(locBoss);
		boss = tk.getImage(urlBoss);
		boss = boss.getScaledInstance(frame.getWidth() / 18, frame.getHeight() / 16, Image.SCALE_SMOOTH);

		seedMap();
	}

	/**
	 * randomly places various Obstacles, enemies, and chests inside the map[][]
	 * array
	 */
	public void seedMap() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				int x = rand.nextInt(1000);
				if (x < 500) {
					int r = rand.nextInt(6);
					if (r == 0) {
						map[i][j] = new Obstacle(obstacle1);
					} else if (r == 1) {
						map[i][j] = new Obstacle(obstacle2);
					} else if (r == 2) {
						map[i][j] = new Obstacle(obstacle3);
					} else if (r == 3) {
						map[i][j] = new Obstacle(obstacle4);
					} else if (r == 4) {
						map[i][j] = new Obstacle(obstacle5);
					} else if (r == 5) {
						map[i][j] = new Obstacle(obstacle6);
					}
				} else if (x > 500 && x < 700) {
					int r = rand.nextInt(7);
					switch (r) {
					case 4:
					case 0:
						map[i][j] = new Enemy("Goomba", enemy1, (rand.nextInt(10 + 3) + stage) * stage);
						break;
					case 5:
					case 1:
						map[i][j] = new Enemy("Dragon", enemy2, (rand.nextInt(10 + 3) + stage) * stage);
						break;
					case 6:
					case 2:
						map[i][j] = new Enemy("Evil Knight", enemy3, (rand.nextInt(10 + 3) + stage) * stage);
						break;
					case 3:
						map[i][j] = new Enemy("Mimic", chest, (rand.nextInt(10 + 3) + stage) * stage);
						break;
					}
				} else if (x > 700 && x < 730) {
					map[i][j] = new Chest();
				} else if (x > 730 && x < 750) {
					map[i][j] = new HealthPotion();
				} else {
					map[i][j] = new Open();
				}
			}
		}
		makePath();
		map[14][15] = new Enemy("Pete", boss, rand.nextInt(30) * stage + 10);
	}

	/**
	 * creates a path after filling the array to ensure the player can always reach
	 * the exit
	 */
	public void makePath() {
		map[0][0] = new Open();
		map[1][0] = new Open();
		map[1][1] = new Open();
		map[0][1] = new Open();
		map[1][2] = new Open();
		map[3][3] = new Open();
		map[1][3] = new Open();
		map[2][3] = new Open();
		map[3][3] = new Open();
		map[3][2] = new Open();
		map[3][1] = new Open();
		map[4][1] = new Open();
		map[2][4] = new Open();
		map[2][5] = new Open();
		map[2][6] = new Open();
		map[3][6] = new Open();
		map[4][6] = new Open();
		map[4][7] = new Open();
		map[5][7] = new Open();
		map[5][8] = new Open();
		map[6][8] = new Open();
		map[7][8] = new Open();
		map[8][8] = new Open();
		map[8][9] = new Open();
		map[8][10] = new Open();
		map[8][11] = new Open();
		map[7][11] = new Open();
		map[7][12] = new Open();
		map[7][13] = new Open();
		map[8][13] = new Open();
		map[9][13] = new Open();
		map[9][14] = new Open();
		map[10][14] = new Open();
		map[10][15] = new Open();
		map[11][15] = new Open();
		map[12][15] = new Open();
		map[13][15] = new Open();
		map[14][15] = new Open();
		map[5][1] = new Open();
		map[5][2] = new Open();
		map[6][2] = new Open();
		map[7][2] = new Open();
		map[7][3] = new Open();
		map[8][3] = new Open();
		map[8][4] = new Open();
		map[9][4] = new Open();
		map[10][4] = new Open();
		map[11][4] = new Open();
		map[12][4] = new Open();
		map[12][5] = new Open();
		map[12][6] = new Open();
		map[12][7] = new Open();
		map[11][7] = new Open();
		map[11][8] = new Open();
		map[11][9] = new Open();
		map[12][9] = new Open();
		map[12][10] = new Open();
		map[13][10] = new Open();
		map[13][11] = new Open();
		map[13][12] = new Open();
		map[14][12] = new Open();
		map[14][13] = new Open();
		map[14][14] = new Open();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// paint for name selection
		if (setName) {
			g.setFont(new Font("Gabriola", Font.PLAIN, 28));
			g.drawString("What is your name? ", 100, 100);
			g.drawString(name, cursorX - (15 * name.length()), 150);
			if (flash) {
				g.drawRect(cursorX, 150, 10, 1);
			}
		}

		// paint for race selection
		if (setRace) {
			g.setFont(new Font("Gabriola", Font.PLAIN, 72));
			g.drawString("Choose a race: ", 100, 100);
			g.setFont(new Font("Gabriola", Font.PLAIN, 28));
			for (int i = 0; i < menuChoices.size(); i++) {
				g.drawString(menuChoices.get(i), 100 + (100 * i), menuY);
			}
			if (flash) {

				g.drawRect(selectX, selectY, 100, 100);
			}
		}

		// paint for class selection
		if (setClass) {
			g.setFont(new Font("Gabriola", Font.PLAIN, 72));
			g.drawString("Choose a character: ", 100, 100);
			g.setFont(new Font("Gabriola", Font.PLAIN, 28));
			for (int i = 0; i < menuChoices.size(); i++) {
				g.drawString(menuChoices.get(i), 100 + (100 * i), menuY);
			}
			if (flash) {

				g.drawRect(selectX, selectY, 100, 100);
			}
		}

		// paint for map display
		if (adventure) {
			g2.drawImage(background, 0, 0, this);
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					g.drawRect(i * (frame.getWidth() / 16), j * (frame.getHeight() / 16), (frame.getWidth() / 16),
							(frame.getHeight() / 16));
				}
			}
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map.length; j++) {
					if (map[i][j] instanceof Obstacle) {
						g2.drawImage(((Obstacle) map[i][j]).img, j * (frame.getWidth() / 16),
								i * (frame.getHeight() / 16), this);
					} else if (map[i][j] instanceof Enemy) {
						g2.drawImage(((Enemy) map[i][j]).img, j * (frame.getWidth() / 16) + 10,
								i * (frame.getHeight() / 16) + 5, this);
					} else if (map[i][j] instanceof Chest) {
						g2.drawImage(chest, j * (frame.getWidth() / 16) + 20, i * (frame.getHeight() / 16) + 10, this);
					} else if (map[i][j] instanceof Boss) {
						g2.drawImage(boss, j * (frame.getWidth() / 16), i * (frame.getHeight() / 16), this);
					} else if (map[i][j] instanceof HealthPotion) {
						g2.drawImage(healthPotion, j * (frame.getWidth() / 16) + 25, i * (frame.getHeight() / 16) + 15,
								this);
					}
				}
			}
			g2.drawImage(character, charX + 20, charY, this);
		}

		// paint for all combat phases
		if (combat) {
			g2.drawImage(combatBack, 0, 0, this);
			g.setFont(new Font("Gabriola", Font.PLAIN, 28));
			g.drawString(curEnemy.get_race(), 810, 40);
			if (flash) { // bounces enemy image
				g2.drawImage(curEnemy.img, enemyPoint.x, enemyPoint.y, this);
			} else {
				g2.drawImage(curEnemy.img, enemyPoint.x, enemyPoint.y + 10, this);
			}
			g2.drawImage(character, heroPoint.x, heroPoint.y, this);
			g.drawRect(heroPoint.x, enemyPoint.y, enemyPoint.x - heroPoint.x, heroPoint.y - enemyPoint.y);
			g.setFont(new Font("Gabriola", Font.PLAIN, 20));
			playerHealth(g, heroPoint.x + 120, heroPoint.y + 30);
			enemyHealth(g, enemyPoint.x + 10, enemyPoint.y + 170);
		}

		// paint for attack phase
		if (combatAttack) {
			g.setColor(Color.red);
			for (int i = 0; i < circles.size(); i++) {
				drawCenteredCircle(g, circles.get(i).x - 7, circles.get(i).y - 30, circleSize);
			}
			drawGreenCircles(g);
			g.setColor(Color.black);
			String inter = String.valueOf(interval).substring(0, Math.min(String.valueOf(interval).length(), 4));
			g.setFont(new Font("Gabriola", Font.PLAIN, 36));
			g.drawString("Time: " + inter, heroPoint.x, enemyPoint.y - 20);
			g.drawString("Damage = " + checkDamage(), heroPoint.x + 10, heroPoint.y - 20);
		}

		// Paint for defense during combat
		if (combatDefense) {
			defenseRect(g);
			g.setColor(Color.black);
			g.fillRect(enemyAttackBar.x, enemyAttackBar.y, 5, 100);
		}

		// paint for the inventory system
		if (inventory) {
			g2.drawImage(inventoryBack, 0, 0, this);
			g.setFont(new Font("Gabriola", Font.PLAIN, 36));
			g.drawString(mainCharacter.get_name(), 510, 560);
			g.drawString("Stage: " + stage, 50, 50);
			playerHealth(g, 50, 70);
			if (flash) {
				g2.drawImage(character, 520, 360, this);
			} else {
				g2.drawImage(character, 520, 370, this);
			}

			// draw the players equipped stuff
			g.drawString("Weapon", 490, 290);
			g.drawString("Armor", 595, 290);
			g.drawRect(505, 300, 50, 50);
			if (mainCharacter.weapon != null) {
				if (mainCharacter.weapon instanceof Axe) {
					g2.drawImage(axe, 505, 300, this);
				} else if (mainCharacter.weapon instanceof Sword) {
					g2.drawImage(sword, 505, 300, this);
				} else if (mainCharacter.weapon instanceof Bow) {
					g2.drawImage(bow, 505, 300, this);
				}
			}
			if (mainCharacter.armor != null) {
				if (mainCharacter.armor instanceof Armor) {
					g2.drawImage(armor, 595, 300, this);
				}
			}
			g.drawRect(590, 300, 50, 50);

			// the inventory doesn't actually have a max size but it will only show the
			// first 28 items
			// draws the players inventory
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 4; j++) {
					g.setColor(Color.black);
					if (i == menuI && j == menuJ) {
						g.setColor(Color.red);
					}
					g.drawRect(200 + (j * 55), 250 + (i * 55), 50, 50);
					int index = (i * 4) + j; // index is the point in the arraylist _inventory that lines up with the 2d
												// array for displaying
					if (mainCharacter.inventoryContains(index)) {
						if (mainCharacter.getItem(index) instanceof Sword) {
							g2.drawImage(sword, 200 + (j * 55), 250 + (i * 55), this);
						} else if (mainCharacter.getItem(index) instanceof Axe) {
							g2.drawImage(axe, 200 + (j * 55), 250 + (i * 55), this);
						} else if (mainCharacter.getItem(index) instanceof Bow) {
							g2.drawImage(bow, 200 + (j * 55), 250 + (i * 55), this);
						} else if (mainCharacter.getItem(index) instanceof Armor) {
							g2.drawImage(armor, 200 + (j * 55), 250 + (i * 55), this);
						}

					}
				}
			}
			int index = (menuI * 4) + menuJ;
			if (mainCharacter.inventoryContains(index)) {
				g.drawString(mainCharacter.getItem(index).toString(), 200, 200);
			}
		}

		// paint for the reward screen
		if (reward) {
			g2.drawImage(rewardBack, 0, 0, this);
			Item r = rewardItem;
			if (r instanceof Axe) {
				g2.drawImage(axe, 400, 300, this);
			} else if (r instanceof Sword) {
				g2.drawImage(sword, 400, 300, this);
			} else if (r instanceof Bow) {
				g2.drawImage(bow, 400, 300, this);
			} else if (r instanceof Armor) {
				g2.drawImage(armor, 400, 300, this);
			}
			g.setFont(new Font("Gabriola", Font.PLAIN, 48));
			g.drawString(rewardItem.toString(), 300, 250);
		}
	}

	/**
	 * Draws the rectangle for defense
	 * 
	 * @param g
	 */
	public void defenseRect(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(200, 300, 400, 50);
		g.setColor(Color.red);
		g.fillRect(200, 300, 75, 50);
		g.fillRect(525, 300, 75, 50);
		g.setColor(Color.yellow);
		g.fillRect(275, 300, 100, 50);
		g.fillRect(425, 300, 100, 50);
		g.setColor(Color.green);
		g.fillRect(375, 300, 50, 50);
	}

	/**
	 * Draws the players health bar with however much HP they currently have
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void playerHealth(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.drawRect(x, y, mainCharacter.getMaxHealth() * 3, 30);
		g.setColor(Color.green);
		g.fillRect(x, y, mainCharacter.get_health() * 3, 30);
		g.setColor(Color.black);
		g.drawString(mainCharacter.get_health() + "/" + mainCharacter.getMaxHealth(),
				x + mainCharacter.getMaxHealth() * 2 - 30, y + 20);
	}

	/**
	 * Draws the enemy health bar with current HP
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public void enemyHealth(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.drawRect(x, y, curEnemy.getMaxHealth() * 3, 30);
		g.setColor(Color.red);
		g.fillRect(x, y, curEnemy.get_health() * 3, 30);
		g.setColor(Color.black);
		g.drawString(curEnemy.get_health() + "/" + curEnemy.getMaxHealth(), x + 5, y + 15);
	}

	/**
	 * draws a circle with the center being the given cords
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param r
	 */
	public void drawCenteredCircle(Graphics g, int x, int y, int r) {
		x = x - (r / 2);
		y = y - (r / 2);
		g.fillOval(x, y, r, r);
	}

	/**
	 * color hit circles green
	 * 
	 * @param g
	 */
	public void drawGreenCircles(Graphics g) {
		for (int j = 0; j < circlesHit.size(); j++) {
			g.setColor(Color.green);
			drawCenteredCircle(g, circlesHit.get(j).x - 7, circlesHit.get(j).y - 30, circleSize);
		}
	}

	/**
	 * Initialize the race for the character
	 */
	public void setRace() {
		menuChoices.add("Human");
		menuChoices.add("Dwarf");
		menuChoices.add("Elf");
		setRace = true;
		repaint();
	}

	/**
	 * Initialize the class for the character
	 */
	public void setClass() {
		setClass = true;
		menuChoices.clear();
		menuSelect = 0;
		selectX = 90;
		selectY = 150;
		menuChoices.add("Knight");
		menuChoices.add("Wizard");
		menuChoices.add("Ranger");
		repaint();
	}

	/**
	 * calculates the damage to the enemy based on how many and how fast circles
	 * were clicked
	 * 
	 * @return
	 */
	public int checkDamage() {
		int hits = 0;
		int damage = 0;
		for (int i = 0; i < circles.size(); i++) {
			for (int j = 0; j < mouseClicks.size(); j++) {
				if (circles.get(i).distance(mouseClicks.get(j)) < circleSize / 2) {
					hits++;
				}
			}
		}
		// only do damage if player hits at least 1 circle
		// only add time multiplier if over 1 second is left
		if (hits >= 1) {
			if ((int) interval > 0) {
				damage = (int) (hits * interval);
			} else {
				damage = hits;
			}
			if (mainCharacter.weapon != null) {
				damage += mainCharacter.weapon.get_level();
			}
		}
		return damage;
	}

	/**
	 * Adjust images for combat and sets combat = true
	 */
	public void startCombat() {
		combat = true;
		curEnemy = (Enemy) map[charI][charJ];
		curEnemy.img = curEnemy.img.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
		character = character.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
	}

	/**
	 * Start the attack phase
	 */
	public void startCombatAttack() {
		combatAttack = true;
		// creates circles to hit
		for (int i = 0; i < stage + 2; i++) {
			circles.add(new Point(rand.nextInt(enemyPoint.x) + heroPoint.x, rand.nextInt(heroPoint.y) + enemyPoint.y));
		}
		// timer for hitting all circles
		timer = new Timer();
		interval = 4;
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				setInterval();
				repaint();
			}
		}, 100, 100);
	}

	/**
	 * end attack phase
	 */
	public void endCombatAttack() {
		timer.cancel();
		curEnemy.sub_health(checkDamage());
		combatAttack = false;
		repaint();
		circles.clear();
		mouseClicks.clear();
		circlesHit.clear();
		if (curEnemy.get_health() <= 0) {
			combatFinished();
			return;
		}
		startCombatDefense();
	}

	/**
	 * start defense phase
	 */
	public void startCombatDefense() {
		combatAttack = false;
		repaint();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		combatDefense = true;
		enemyAttackBar = new Point(600, 275);

		// set speed of bar based on armor
		// the lower the speed the harder for the player
		int speed = 10;
		if (mainCharacter.armor != null) {
			speed = rand.nextInt(mainCharacter.armor.get_level()) + 5 - (curEnemy.get_health() / stage);
			if (speed <= 0) {
				speed = 1;
			}
		} else {
			speed = rand.nextInt(5) + 1;
		}

		// timer for defense bar reaction
		interval = 21;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				interval -= .05;
				enemyAttackBar.x--;
				if (interval <= 0) {
					endCombatDefense();
				}
				repaint();
			}
		}, speed, speed);
	}

	/**
	 * end defense phase, decide amount of damage parent takes
	 */
	public void endCombatDefense() {
		timer.cancel();
		combatDefense = false;
		if (enemyAttackBar.x < 275 || enemyAttackBar.x > 525) {
			mainCharacter.sub_health(10);
		} else if (enemyAttackBar.x > 375 && enemyAttackBar.x < 425) {

		} else {
			mainCharacter.sub_health(5);
		}
		repaint();
	}

	/**
	 * helper function for attack timer
	 */
	private final void setInterval() {
		if (interval <= 0.1) {
			endCombatAttack();
		}
		interval -= .1;
	}

	/**
	 * clean up variables and image changes from the combat and go back to adventure
	 * view
	 */
	public void combatFinished() {
		curEnemy.img = curEnemy.img.getScaledInstance(frame.getWidth() / 20, frame.getHeight() / 20,
				Image.SCALE_SMOOTH);
		if (curEnemy.get_race().equals("Pete")) {
			newStage();
		}
		circles.clear();
		mouseClicks.clear();
		character = character.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

		// greater chance at getting a reward if the enemy is tougher
		int x = rand.nextInt(curEnemy.getMaxHealth()) + stage;
		if (x > rand.nextInt(stage * 10)) {
			getReward();
		} else {
			combat = false;
			adventure = true;
		}
	}

	/**
	 * function to increase stage and re-seed map
	 */
	public void newStage() {
		stage++;
		seedMap();
		mainCharacter.set_health(mainCharacter.getMaxHealth());
		charI = 0;
		charJ = 0;
	}

	/**
	 * function to create reward items after fights or chests
	 */
	public void getReward() {
		sword = sword.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		axe = axe.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		bow = bow.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		armor = armor.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		reward = true;
		combat = false;
		int x = rand.nextInt(4);
		switch (x) {
		case 0:
			rewardItem = new Axe(stage + x, "axe");
			break;
		case 1:
			rewardItem = new Sword(stage + x, "sword");
			break;
		case 2:
			rewardItem = new Bow(stage + x, "bow");
			break;
		case 3:
			rewardItem = new Armor(stage, "armor");
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (reward) {
			// accept item
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				adventure = true;
				mainCharacter.addToInventory(rewardItem);
				reward = false;
				sword = sword.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				axe = axe.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				bow = bow.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				armor = armor.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
			}
			// reject item
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				adventure = true;
				reward = false;
				sword = sword.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				axe = axe.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				bow = bow.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				armor = armor.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
			}
		}
		if (inventory) {
			// exit inventory
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				character = character.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				inventory = false;
				adventure = true;
			}
			// navigate inventory
			if (e.getKeyCode() == KeyEvent.VK_W) {
				menuI--;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				menuI++;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				menuJ--;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				menuJ++;
			}
			// swap item with equipped item
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				int index = (menuI * 4) + menuJ;
				if (mainCharacter.inventoryContains(index)) {
					if (mainCharacter.get_inventory().get(index) instanceof Weapon) {
						mainCharacter.weapon = mainCharacter.get_inventory().get(index);
					} else if (mainCharacter.get_inventory().get(index) instanceof Armor) {
						mainCharacter.armor = mainCharacter.get_inventory().get(index);
					}
				}
			}
			repaint();
		}
		// stop defense bar
		if (combatDefense) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				endCombatDefense();
			}
		}
		// control character around map, open inventory
		if (adventure) {
			if (e.getKeyCode() == KeyEvent.VK_I) {
				adventure = false;
				inventory = true;
				character = character.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
				repaint();
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				if (charI > 0) {
					if (!(map[charI - 1][charJ] instanceof Obstacle)) {
						charI--;
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				if (charI < map.length - 1) {
					if (!(map[charI + 1][charJ] instanceof Obstacle)) {
						charI++;
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				if (charJ < map.length - 1) {
					if (!(map[charI][charJ + 1] instanceof Obstacle)) {
						charJ++;
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				if (charJ > 0) {
					if (!(map[charI][charJ - 1] instanceof Obstacle)) {
						charJ--;
					}
				}
			}
			// repaint character
			charX = chunkSizeX * charJ;
			charY = chunkSizeY * charI;
			repaint();
			// if character is on an action space perform action
			if (map[charI][charJ] instanceof Enemy) {
				adventure = false;
				startCombat();
				map[charI][charJ] = new Open();
			} else if (map[charI][charJ] instanceof Chest) {
				adventure = false;
				getReward();
				map[charI][charJ] = new Open();
			} else if (map[charI][charJ] instanceof HealthPotion) {
				mainCharacter.add_health(5 * stage);
				map[charI][charJ] = new Open();
			}
		}
		if (setName) {
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				cursorX -= 15;
				name = name.substring(0, name.length() - 1);
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				setName = false;
				setRace();
			} else {
				name += e.getKeyChar();
				cursorX += 15;
				repaint();
			}
		}
		if (setRace || setClass) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (menuSelect < menuChoices.size() - 1) {
					menuSelect++;
					selectX += 100;
					repaint();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (menuSelect > 0) {
					menuSelect--;
					selectX -= 100;
					repaint();
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (setRace) {
				switch (menuSelect) {
				case 0:
					race = "human";
					break;
				case 1:
					race = "dwarf";
					break;
				case 2:
					race = "elf";
					break;
				}
				setRace = false;
				setClass();
			}
		}
		if (setClass) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				switch (menuSelect) {
				case 0:
					mainCharacter = new Knight(name, race);
					break;
				case 1:
					mainCharacter = new Wizard(name, race);
					break;
				case 2:
					mainCharacter = new Ranger(name, race);
					break;
				}
				setClass = false;
				initCharacter();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void run() {
		try {
			while (true) {
				flash = !flash; // used to flash menu selection box and to bounce characters
				repaint();
				Thread.sleep(500);
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (combatAttack) {
			if (mouseClicks.size() < circles.size()) {
				mouseClicks.add(new Point(e.getX(), e.getY()));
				for (int i = 0; i < circles.size(); i++) {
					for (int j = 0; j < mouseClicks.size(); j++) {
						if (circles.get(i).distance(mouseClicks.get(j)) < circleSize / 2) {
							circlesHit.add(circles.get(i));
						}
					}
				}
				if (mouseClicks.size() == circles.size()) {
					endCombatAttack();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (combat && !combatAttack && !combatDefense) {
			if (e.getX() > heroPoint.x && e.getX() < enemyPoint.getX() && e.getY() < heroPoint.getY()
					&& e.getY() > enemyPoint.getY()) {
				startCombatAttack();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
