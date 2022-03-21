       import static java.util.concurrent.TimeUnit.MILLISECONDS;
	import static java.util.logging.Level.FINE;
	import static java.util.logging.Level.INFO;
	import static java.util.logging.Level.SEVERE;
	
	import java.io.IOException;
	import java.net.InetSocketAddress;
	import java.net.SocketAddress;
	import java.nio.ByteBuffer;
	import java.nio.channels.AsynchronousChannelGroup;
	import java.nio.channels.AsynchronousCloseException;
	import java.nio.channels.AsynchronousServerSocketChannel;
	import java.nio.channels.AsynchronousSocketChannel;
	import java.nio.channels.CompletionHandler;
	import java.nio.charset.Charset;
	import java.util.concurrent.ThreadFactory;
	import java.util.concurrent.atomic.AtomicInteger;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	
	/**
	 * A sample async server based on AsynchronousServerSocketChannel.
	 */
	public class AsyncServer2 {
	    private static final Charset UTF8 = Charset.forName("UTF-8");
	    private static final Logger logger =
	        Logger.getLogger(AsyncServer2.class.getName());
	    private static final int MAXIN = 1024;
	    private static final int MAXOUT = 1024;
	
	    private final AsynchronousChannelGroup channelGroup;
	    private final AsynchronousServerSocketChannel serverChannel;
	
	    /**
	     * Main program to run the server.  Logs the server socket port at INFO if
	     * no port is specified.
	     *
	     * @param args host and optional port
	     */
	    public static void main(String[] args) throws Exception {
	        final int port = args.length > 0 ? Integer.parseInt(args[0]) : 0;
	        final AsynchronousChannelGroup channelGroup =
	            AsynchronousChannelGroup.withFixedThreadPool(
	                2,
	                new ThreadFactory() {
	                    final AtomicInteger id = new AtomicInteger(0);
	                    @Override
	                    public Thread newThread(Runnable r) {
	                        return new Thread(
	                            r, "AsyncServer2-" + id.incrementAndGet());
	                    }
	                });
	        final AsyncServer2 asyncServer =
	            new AsyncServer2(new InetSocketAddress(port), channelGroup);
	        final Level level = (port == 0) ? INFO : FINE;
	        if (logger.isLoggable(level)) {
	            logger.log(level,
	                       "Accepting connections on port " +
	                       asyncServer.getLocalPort());
	        }
	        channelGroup.awaitTermination(Long.MAX_VALUE, MILLISECONDS);
	    }
	
	    /**
	     * Creates a server.
	     *
	     * @param address the server address
	     * @param channelGroup the thread group for handling requests
	     */
	    public AsyncServer2(SocketAddress address,
	                        AsynchronousChannelGroup channelGroup)
	        throws IOException {
	
	        this.channelGroup = channelGroup;
	        serverChannel = AsynchronousServerSocketChannel.open(channelGroup)
	            .bind(address);
	        accept();
	    }
	
	    /**
	     * Returns the port that the server is listening on.
	     *
	     * @return the server port
	     */
	    int getLocalPort()
	        throws IOException {
	
	        final SocketAddress address = serverChannel.getLocalAddress();
	        if (address instanceof InetSocketAddress) {
	            return ((InetSocketAddress) address).getPort();
	        }
	        return 0;
	    }
	
	    /**
	     * Register handler for new connections.
	     */
	    void accept() {
	        serverChannel.accept(null, new Acceptor());
	    }
	
	    /**
	     * Handler to listen for new connections.
	     */
	    class Acceptor
	            implements CompletionHandler<AsynchronousSocketChannel, Void> {
	
	        @Override
	        public void completed(AsynchronousSocketChannel channel,
	                              Void attachment) {
	            accept();
	            new ChannelHandler(channel);
	        }
	
	        @Override
	        public void failed(Throwable exception, Void attachment) {
	            accept();
	            logger.log(SEVERE, "Unexpected exception", exception);
	        }
	    }
	
	    /**
	     * Handler for a connection.
	     */
	    static class ChannelHandler
	            implements CompletionHandler<Integer, Void> {
	        private final AsynchronousSocketChannel channel;
	        private final ByteBuffer in = ByteBuffer.allocate(MAXIN);
	        private final ByteBuffer out = ByteBuffer.allocate(MAXOUT);
	        private volatile boolean reading = true;
	
	        ChannelHandler(AsynchronousSocketChannel channel) {
	            this.channel = channel;
	            channel.read(in, null, this);
	        }
	
	        @Override
	        public void completed(Integer numBytes, Void attachment) {
	            if (reading) {
	                handleRead();
	            } else {
	                handleWrite();
	            }
	        }
	
	        void handleRead() {
	            if (inputIsComplete()) {
	                process();
	                reading = false;
	                channel.write(out, null, this);
	            }
	        }
	
	        boolean inputIsComplete() {
	            if (in.position() < 2) {
	                return false;
	            }
	            final int numBytes = in.getShort(0);
	            return in.position() >= 2 + numBytes;
	        }
	
	        void handleWrite() {
	            if (outputIsComplete()) {
	                reading = true;
	            }
	        }
	
	        boolean outputIsComplete() {
	            return out.remaining() == 0;
	        }
	
	        void process() {
	            in.flip().position(2);
	            out.clear();
	            out.position(2);
	            out.put(UTF8.encode("Got: "));
	            out.put(in);
	            out.putShort(0, (short) (out.position() - 2)).flip();
	        }
	
	        @Override
	        public void failed(Throwable exception, Void attachment) {
	            if (exception instanceof AsynchronousCloseException) {
	                return;
	            }
	            logger.log(SEVERE, "Unexpected exception", exception);
	        }
	    }
	}
