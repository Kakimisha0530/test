package aicha;

public enum Stations {
	AUTRE(5000,new String[]{}),
	CLASSIQUE(5001,new String[]{}),
	ROCK(5002,new String[]{});

	private int port;
	private String[] playlist;

	private Stations(int p,String[] pl) {
		this.port = p;
	}

	public int getPort() {
		return port;
	}
}