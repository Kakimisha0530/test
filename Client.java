package aicha;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client extends Thread {
	// private ByteBuffer buffer = ByteBuffer.allocate(48);
	private SocketChannel client;
	private CharSequence rep = "ICY 200 OK ";
	private Stations station;

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
		Scanner sc = new Scanner(System.in);
		String ch = "";
		boolean stop = false;

		try {
			while (!this.client.finishConnect()) {
				// attendre que le client se connecte
			}
			while (!stop) {
				// see if any message has been received
				ByteBuffer bufferA = ByteBuffer.allocate(20);
				String content = "";
				boolean pass = false;
				if (this.client.read(bufferA) != -1)
				{
					bufferA.flip();
					while (bufferA.hasRemaining()) {
						try {
							byte cha = bufferA.get();
							if (cha == 10 || cha == 13)// && bufferA.getChar(pos
														// + 1) == '\n')
								pass = true;
							else {
								content += (char) cha;
							}
						}
						catch (BufferUnderflowException e) {
							pass = true;
							continue;
						}
					}
					System.out.println("fin");
					bufferA.clear();
				}
				
				if (content.contains(this.rep))
					System.out.println("DEBUT DU CHARGEMENT \n\n");
				System.out.println("res : " + content);
				if (sc.hasNext() && (ch = sc.nextLine()) != "") {
					ByteBuffer buffer = ByteBuffer.wrap(("0").getBytes());
					while (buffer.hasRemaining()) {
						this.client.write(buffer);
					}
					stop = true;
					buffer.clear();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String writeContent() throws IOException {
		System.out.println("WRITING");
		String ch = "";
		ByteBuffer buffer = ByteBuffer.allocate(8);
		while (this.client.read(buffer) != -1) {
			System.out.println("flip ");
			buffer.flip();
			if (buffer.hasRemaining()) {
				ch += new String(buffer.array());
				System.out.println("res 1 : " + ch);
			}
			buffer.clear();
		}
		System.out.println("res : " + ch);
		return ch;
	}
}
