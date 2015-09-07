package org.harper.otms.common.i18n;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferedHttpServletResponseWrapper extends
		HttpServletResponseWrapper {

	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	private PrintWriter writer = new PrintWriter(buffer);

	private ServletOutputStream os = new ByteArrayServletOutputStream(buffer);

	public BufferedHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	private int statusCode;

	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
		this.statusCode = sc;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return os;
	}

	public byte[] getBufferContent() {
		return buffer.toByteArray();
	}

	public void discard() {
		try {
			this.buffer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
