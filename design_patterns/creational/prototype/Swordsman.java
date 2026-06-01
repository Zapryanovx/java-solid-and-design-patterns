package design_patterns.creational.prototype;

public class Swordsman extends GameUnit {

	public void attack() {
		this.state = "attacking";
	}
}
