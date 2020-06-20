package com.syntaxphoenix.syntaxapi.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.syntaxphoenix.syntaxapi.threading.SynThreadFactory;
import com.syntaxphoenix.syntaxapi.utils.java.Keys;

public abstract class AsyncSocketServer extends SocketServer {

	private final ExecutorService service;

	public AsyncSocketServer() {
		this(new SynThreadFactory(Keys.generateKey(8)));
	}

	public AsyncSocketServer(SynThreadFactory factory) {
		this(factory, Executors.newCachedThreadPool(factory));
	}

	public AsyncSocketServer(ExecutorService service) {
		this(DEFAULT_PORT, service);
	}

	public AsyncSocketServer(SynThreadFactory factory, ExecutorService service) {
		this(DEFAULT_PORT, factory, service);
	}

	public AsyncSocketServer(int port) {
		this(port, new SynThreadFactory(Keys.generateKey(8)));
	}

	public AsyncSocketServer(int port, SynThreadFactory factory) {
		this(port, factory, Executors.newCachedThreadPool(factory));
	}

	public AsyncSocketServer(int port, ExecutorService service) {
		super(port);
		this.service = service;
	}

	public AsyncSocketServer(int port, SynThreadFactory factory, ExecutorService service) {
		super(port, factory);
		this.service = service;
	}

	public AsyncSocketServer(int port, InetAddress address) {
		this(port, address, new SynThreadFactory(Keys.generateKey(8)));
	}

	public AsyncSocketServer(int port, InetAddress address, SynThreadFactory factory) {
		this(port, address, factory, Executors.newCachedThreadPool(factory));
	}

	public AsyncSocketServer(int port, InetAddress address, ExecutorService service) {
		super(port, address);
		this.service = service;
	}

	public AsyncSocketServer(int port, InetAddress address, SynThreadFactory factory, ExecutorService service) {
		super(port, address, factory);
		this.service = service;
	}

	/*
	 * 
	 */

	public final ExecutorService getExecutorSerivce() {
		return service;
	}

	/*
	 * 
	 */

	@Override
	protected void handleClient(Socket socket) throws Throwable {
		service.execute(() -> {
			try {
				handleClientAsync(socket);
			} catch (Throwable throwable) {
				try {
					if(!socket.isClosed())
						socket.close();
				} catch (IOException e) {
				}
				handleExceptionAsync(throwable);
			}
		});
	}

	protected void handleExceptionAsync(Throwable throwable) {
		throwable.printStackTrace();
	}

	protected abstract void handleClientAsync(Socket socket) throws Throwable;

}
