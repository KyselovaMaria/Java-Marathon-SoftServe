class Pizza {
	private String cheese;
	private String meat;
	private String seafood;
	private String vegetable;
	private String mushroom;

	private Pizza() { }

	public String getCheese() {
		return cheese;
	}

	public String getMeat() {
		return meat;
	}

	public String getSeafood() {
		return seafood;
	}

	public String getVegetable() {
		return vegetable;
	}

	public String getMushroom() {
		return mushroom;
	}

	public static PizzaBuilder base() {
		return new PizzaBuilder();
	}

	public static class PizzaBuilder {
		private String cheese;
		private String meat;
		private String seafood;
		private String vegetable;
		private String mushroom;

		private PizzaBuilder() {
		}

		public PizzaBuilder addCheese(String cheese) {
			this.cheese = cheese;
			return this;
		}

		public PizzaBuilder addMeat(String meat) {
			this.meat = meat;
			return this;
		}

		public PizzaBuilder addSeafood(String seafood) {
			this.seafood = seafood;
			return this;
		}

		public PizzaBuilder addVegetable(String vegetable) {
			this.vegetable = vegetable;
			return this;
		}

		public PizzaBuilder addMushroom(String mushroom) {
			this.mushroom = mushroom;
			return this;
		}

		public Pizza build() {
			Pizza pizza = new Pizza();
			pizza.cheese = this.cheese;
			pizza.meat = this.meat;
			pizza.seafood = this.seafood;
			pizza.vegetable = this.vegetable;
			pizza.mushroom = this.mushroom;
			return pizza;
		}
	}
}

class Oven {
	public static Pizza cook() {
		return Pizza.base()
				.addCheese("Mozzarella")
				.addMeat("Pepperoni")
				.addVegetable("Mushrooms")
				.build();
	}
}
