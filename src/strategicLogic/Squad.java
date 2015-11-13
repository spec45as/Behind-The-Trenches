package strategicLogic;

public class Squad implements Cloneable {
	private String id;
	private Team team;
	private int costMoney;
	private int costPeople;
	private String name;
	private String description;
	private int peopleAmount;

	public Squad clone() {
		Squad newSquad = null;

		try {
			newSquad = (Squad) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return newSquad;
	}

	Squad(String ID, Team team, int money, int people, int soldiers, String name, String description) {
		setID(ID);
		setTeamOfSquad(team);
		setCostMoney(money);
		setCostPeople(people);
		setPeopleAmount(soldiers);
		setName(name);
		setDescription(description);
	}

	public Squad() {
		// TODO Auto-generated constructor stub
	}

	void setID(String id) {
		this.id = id;
	}

	void setCostMoney(int money) {
		costMoney = money;
	}

	void setCostPeople(int people) {
		costPeople = people;
	}

	void setName(String name) {
		this.name = name;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public String getID() {
		return id;
	}

	public int getCostMoney() {
		return costMoney;
	}

	public int getCostPeople() {
		return costPeople;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPeopleAmount() {
		return peopleAmount;
	}

	public void setPeopleAmount(int peopleAmount) {
		this.peopleAmount = peopleAmount;
	}

	public Team getTeamOfSquad() {
		return team;
	}

	public void setTeamOfSquad(Team team) {
		this.team = team;
	}
}