package nioserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

	private InetSocketAddress socketAddress;
	private volatile boolean shutdown = false;
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private PollerThread pollThread;

	ExecutorService executors;

	final boolean nonBlockingClient;

	public Server(int port, boolean nonBlockingClient) {
		this.socketAddress = new InetSocketAddress("localhost", port);
		this.nonBlockingClient = nonBlockingClient;
	}

	public void startup() throws IOException, InterruptedException {
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(socketAddress, 0);
		ServerSocket acceptSocket = serverChannel.socket();
		acceptSocket.setSoTimeout(0);
		selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		executors = new ThreadPoolExecutor(10, 100, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		pollThread = new PollerThread();
		pollThread.start();
		System.out.println("Server started at:" + acceptSocket);
		pollThread.join();

	}

	public void shutdown() throws IOException {
		shutdown = true;
		executors.shutdownNow();
		serverChannel.socket().close();
		serverChannel.close();
		selector.close();
		pollThread.interrupt();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		/* Set to false if client is a blocking synchronous client. */
		Server s = new Server(6868, true);
		s.startup();

	}

	class PollerThread extends Thread {
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
						case SelectionKey.OP_ACCEPT:
							SocketChannel channel = serverChannel.accept();
							channel.configureBlocking(false);
							channel.socket().setSoTimeout(0);
							channel.register(selector, SelectionKey.OP_READ);
							break;
						case SelectionKey.OP_READ:
							if (scKey.channel() instanceof SocketChannel) {
								RequestTask task = new RequestTask(scKey, nonBlockingClient);
								executors.submit(task);
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
	}

	class RequestTask implements Runnable {
		protected SocketChannel channel;
		protected SelectionKey scKey;

		public RequestTask(SelectionKey scKey, boolean nonBlockingClient) throws IOException {
			this.scKey = scKey;
			this.channel = (SocketChannel) scKey.channel();
			if (!nonBlockingClient) {
				scKey.cancel();
				this.channel.configureBlocking(true);
			} else {
				this.channel.configureBlocking(false);
			}
		}

		@Override
		public void run() {
			BufferedReader in = null;
			PrintWriter out = null;
			String requestLine;
			in = new BufferedReader(new InputStreamReader(Channels.newInputStream(channel)));
			out = new PrintWriter(Channels.newOutputStream(channel), true);
			while (true) {
				try {
					requestLine = in.readLine();
					if (requestLine.equals("CLOSE")) {
						out.println("Got CLOSE Request, server is closing this socket:" + channel.socket());
						channel.socket().close();
						channel.close();
						return;
					} else if (requestLine.equals("SHUTDOWN")) {
						out.println("Got Server SHUTODOWN Request, server is shutting down now..");
						channel.socket().close();
						channel.close();
						shutdown();
						return;

					}
					out.println("Server has processed the request:" + requestLine);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
