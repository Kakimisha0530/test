package aicha;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Serveur extends Thread {
	private ServerSocketChannel server;
	private CharSequence rep = "ICY 200 OK \r\n";
	private Stations station;

	public Serveur(Stations st) throws IOException {
		this.station = st;
		this.server = ServerSocketChannel.open();
		this.server.configureBlocking(true);
		this.server.socket().bind(new InetSocketAddress(this.station.getPort()));
		
	}

	public Serveur() throws IOException {
		this(Stations.AUTRE);
	}

	@Override
	public void run() {
		System.out.println("Starting server for station : " + this.station.name());
		boolean stop = false;
		while (!stop) {
			SocketChannel sc;
			try {
				sc = this.server.accept();
				if (sc != null) {
					System.out.println("new conection for : " + this.station.name());
					//this.sendContent(sc);
					do{
						try {
							sendContent(sc);
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					while(!(stop = !sc.isConnected()));
					System.out.println("end connection");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally{
				try {
					this.server.close();
					stop = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public boolean listen(SocketChannel sc) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(48);
		String ch = "";
		while (sc.read(buffer) != -1) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				ch += new String(buffer.array());
				System.out.println(ch);
			}
			
			buffer.clear();
		}

		return Utils.isInteger(ch) && Integer.parseInt(ch) == 0;
	}

	private void sendContent(SocketChannel sc) throws IOException {
		System.out.println("sending : " + this.rep);
		ByteBuffer buffer = ByteBuffer.wrap((this.rep.toString()).getBytes());
		sc.write(buffer);
		buffer.clear();
		this.rep = this.station.name();
	}
	
	public static void main(String[] args) {
		try {
			Serveur autre = new Serveur(Stations.AUTRE);
			Serveur cla = new Serveur(Stations.CLASSIQUE);
			Serveur rock = new Serveur(Stations.ROCK);
			autre.start();
			cla.start();
			rock.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
