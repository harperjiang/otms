package org.harper.otms.common.i18n;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ByteArrayServletOutputStream extends ServletOutputStream {

	private OutputStream inner;

	public ByteArrayServletOutputStream(OutputStream inner) {
		this.inner = inner;
	}

	@Override
	public void write(int b) throws IOException {
		this.inner.write(b);
	}
}
