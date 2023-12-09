package com.ariel.tomcat.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 可重复读的请求，只需构建一次
 */
public class ReReadableHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] body;

    private ReReadableHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        int available = super.getInputStream().available();
        if (available > 1<<20) {
            return super.getInputStream();
        }
        if (body == null) {
            body = new byte[available];
            super.getInputStream().read(body);
        }
        return new ServletInputStream() {

            private int offset;

            @Override
            public boolean isFinished() {
                return offset == body.length;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                if (isFinished()) {
                    return -1;
                }
                return body[offset++] & 0xff;
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public static ReReadableHttpServletRequest valueOf(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req instanceof ReReadableHttpServletRequest) {
            return ((ReReadableHttpServletRequest) req);
        }
        return new ReReadableHttpServletRequest(req);
    }
}
