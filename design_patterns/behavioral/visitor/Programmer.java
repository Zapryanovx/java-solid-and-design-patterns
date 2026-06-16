package design_patterns.behavioral.visitor;

public class Programmer extends AbstractEmployee {

    private final String skill;

    public Programmer(String name, String skill) {
        super(name);
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }
}
