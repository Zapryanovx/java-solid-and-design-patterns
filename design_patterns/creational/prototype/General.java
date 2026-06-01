package design_patterns.creational.prototype;

// Doesn't support cloning
public class General extends GameUnit {

	public void boostMorale() {
		this.state = "MoralBoost";
	}

	@Override
	public GameUnit clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Generals are unique");
	}
}
