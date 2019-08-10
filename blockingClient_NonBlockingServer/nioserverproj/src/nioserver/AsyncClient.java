package nioserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class AsyncClient {

	private SocketChannel channel;
	private Selector selector;
	private ReactorThread reactor;
	private int msgId = 0;

	public static volatile boolean shutdown = false;

	public AsyncClient() {
		try {
			channel = SocketChannel.open();

			channel.configureBlocking(false);
			selector = Selector.open();
			reactor = new ReactorThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		reactor.start();
		try {
			channel.register(selector, SelectionKey.OP_CONNECT);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (!channel.isConnected() && channel.connect(new InetSocketAddress("localhost", 6868))) {
				System.out.println("Channel connected synchronously");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		AsyncClient client = new AsyncClient();
		client.start();

	}

	class ReactorThread extends Thread {
		@Override
		public void run() {
			while (!shutdown) {
				try {
					int result = selector.select(1000);
					if (result == 0) {
						continue;
					}

					Set<SelectionKey> selectedKeys = selector.selectedKeys();

					for (SelectionKey scKey : selectedKeys) {
						switch (scKey.readyOps()) {
						case SelectionKey.OP_CONNECT:
							System.out.println("OP_CONNECT event");
							if (!channel.isConnected()) {
								channel.connect(new InetSocketAddress("localhost", 6868));
								while (channel.isConnectionPending()) {
									channel.finishConnect();
								}
							}
							if (channel.isConnected()) {
								System.out.println("Client Connected");
							}
							channel.configureBlocking(false);
							channel.socket().setSoTimeout(0);
							channel.socket().setReuseAddress(true);
							channel.register(selector, SelectionKey.OP_READ);
							channel.register(selector, SelectionKey.OP_WRITE);
							break;
						case SelectionKey.OP_READ:
							if (scKey.channel() instanceof SocketChannel) {
								read((SocketChannel) scKey.channel());
							}
							break;
						case SelectionKey.OP_WRITE:
							if (scKey.channel() instanceof SocketChannel) {
								write((SocketChannel) scKey.channel());
							}
							break;
						}
					}

					selectedKeys.clear();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		private void write(SocketChannel socketChannel)  {
			if (socketChannel.isConnected()) {
				BufferedReader in = null;
				PrintWriter out = null;
				in = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(channel.socket().getOutputStream()));
				if (msgId < 10) {
					sendMessage(in, out, "ClientReqId:" + new Integer(msgId++).toString());
				}

				else {
					sendMessage(in, out, "CLOSE");
					// sendMessage(in,out,"SHUTODOWN");
					shutdown = true;
				}
			} else {
				System.out.println("socket not connected");
				shutdown = true;
			}

		}

		private void read(SelectableChannel selectableChannel) {
			System.out.println("Read called on client");

		}

		private void sendMessage(BufferedReader in, PrintWriter out, String msg) {

			try {
				out.println(msg);
				// out.flush();
				System.out.println("Response from Thread:" + Thread.currentThread().getName() + "  " + in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
