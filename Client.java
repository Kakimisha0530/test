package aicha;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client extends Thread {
	private SocketChannel client;
	private CharSequence rep = "ICY 200 OK ";
	private Stations station;

	// private

	public Client(Stations st) throws IOException {
		this.station = st;
		this.client = SocketChannel.open();
		this.client.configureBlocking(true);
		this.client.connect(new InetSocketAddress(this.station.getPort()));
	}

	public Client() throws IOException {
		this(Stations.AUTRE);
	}

	@Override
	public void start() {
		boolean stop = false;

		try {
			while (!this.client.finishConnect()) {
				// attendre que le client se connecte
			}
			while (!stop) {
				// see if any message has been received
				ByteBuffer bufferA = ByteBuffer.allocate(20);
				String content = "";
				if (this.client.read(bufferA) != -1) {
					bufferA.flip();
					while (bufferA.hasRemaining()) {
						try {
							byte cha = bufferA.get();
							content += (char) cha;
						} catch (BufferUnderflowException e) {
							continue;
						}
					}
					bufferA.clear();
				}

				if (content.contains(this.rep))
					System.out.println("DEBUT DU CHARGEMENT \n");
				else {
					if (this.writeContent(content))
						System.out.println(content);
					else
						System.out.println("Impossible d'ecrire le contenu !!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean writeContent(String newLine) throws IOException {
		FileWriter writer = null;
		boolean save = true;
		try {
			newLine += "\n";
			writer = new FileWriter("file.txt", true);
			writer.write(newLine, 0, newLine.length());
		} catch (IOException ex) {
			save = false;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

		return save;
	}
}
