package nioserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {

	public static final int nClientThreads = 3;
	public static final int tasksPerThread = 3;
	public static volatile boolean shutdown = false;
	public static final ExecutorService executors = new ThreadPoolExecutor(10, 100, 10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
	public static final InetSocketAddress serverAddress = new InetSocketAddress("localhost", 6868);

	public static void main(String[] args) throws InterruptedException {
		Future<?>[] taskStatus = new Future<?>[nClientThreads];
		for (int i = 0; i < nClientThreads; i++) {
			taskStatus[i] = executors.submit(new ClientRequestTask(serverAddress, i + 1000));
		}
		for (int i = 0; i < taskStatus.length;) {
			if (taskStatus[i].isDone()) {
				i++;
			}
		}
		//shutdown server.
		try {
			 executors.submit(new ClientRequestTask(serverAddress, -1)).get();
			 System.out.println("Server shutdown complete");
			 shutdown();
			 System.out.println("Client shutdown complete");
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void shutdown() {
		try {
			shutdown = true;
			executors.shutdown();
			executors.awaitTermination(100, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ClientRequestTask implements Runnable {

		InetSocketAddress serverAddress;
		int taskid;

		ClientRequestTask(InetSocketAddress serverAddress, int taskid) {
			this.serverAddress = serverAddress;
			this.taskid = taskid;

		}

		@Override
		public void run() {
			while (!shutdown) {
				SocketChannel channel = null;
				BufferedReader in = null;
				PrintWriter out = null;


				try {
					channel = SocketChannel.open();
					channel.bind(new InetSocketAddress("localhost", 4646 + taskid));
					// channel.configureBlocking(false);
					channel.connect(serverAddress);
					channel.socket().setReuseAddress(true);

					in = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
					out = new PrintWriter(new OutputStreamWriter(channel.socket().getOutputStream()));
					if (taskid == -1) {
						{
							sendMessage(in, out, "SHUTDOWN");
							return;
						}
					}

					for (int i = taskid; i < taskid + tasksPerThread; i++) {
						sendMessage(in, out, "ClientReqId:" + i);
					}
					sendMessage(in, out, "CLOSE");
					
					return;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						channel.socket().close();
						channel.close();
						in.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					} 
					
				}

			}
		}

		private void sendMessage(BufferedReader in,
				PrintWriter out, String msg) {

			try {
				out.println(msg);
				out.flush();
				System.out.println("Response from Thread:" + Thread.currentThread().getName() + "  " + in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
