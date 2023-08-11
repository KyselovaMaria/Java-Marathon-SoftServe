class NameList {
	private final String[] names = {"Mike", "Emily", "Nick", "Patric", "Sara"};

	public Iterator getIterator() {
		return new Iterator();
	}

	public class Iterator implements java.util.Iterator<String> {
		private int counter;

		private Iterator() {
			this.counter = 0;
		}

		public boolean hasNext() {
			return counter < names.length;
		}

		public String next() {
			return names[counter++];
		}
	}
}
