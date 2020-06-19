package com.syntaxphoenix.syntaxapi.net.http;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map.Entry;

public class HttpWriter {
	
	private final PrintStream stream;
	
	public HttpWriter(PrintStream stream) {
		this.stream = stream;
	}
	
	/*
	 * 
	 */
	
	public PrintStream getStream() {
		return stream;
	}
	
	/*
	 * 
	 */
	
	public HttpWriter write(int code) {
		stream.println("HTTP/1.1 " + code + ' ' + ResponseCode.getName(code));
		return this;
	}
	
	/*
	 * 
	 */
	
	public HttpWriter writeServer() {
		return write("Server", "Java SyntaxPhoenix HTTP Server from Lauriichan : 1.0");
	}
	
	public HttpWriter writeDate() {
		return write("Date", new Date());
	}
	
	public HttpWriter writeLength(int length) {
		return write("Content-Length", length);
	}
	
	public HttpWriter writeType(ContentType type) {
		return writeType(type.type());
	}
	
	public HttpWriter writeType(String type) {
		return write("Content-Type", type + "; charset=UTF-8");
	}
	
	/*
	 * 
	 */
	
	public <T extends Object> HttpWriter write(Entry<String, T> entry) {
		return write(entry.getKey(), entry.getValue());
	}
	
	public HttpWriter write(String key, Object value) {
		return write(key, value.toString());
	}
	
	public HttpWriter write(String key, String value) {
		stream.println(key + ": " + value);
		return this;
	}
	
	/*
	 * 
	 */
	
	public HttpWriter write(byte[] buffer) throws IOException {
		stream.write(buffer);
		return this;
	}
	
	public HttpWriter line() {
		stream.println();
		return this;
	}
	
	/*
	 * 
	 */
	
	public HttpWriter clear() {
		stream.flush();
		return this;
	}
	
	public HttpWriter close() {
		stream.close();
		return this;
	}

}
