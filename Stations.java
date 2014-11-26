package aicha;

public enum Stations {
	AUTRE(5000), CLASSIQUE(5001), ROCK(5002);

	private int port;

	private Stations(int p) {
		this.port = p;
	}

	public int getPort() {
		return port;
	}
}