package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n;
    private String login;
    private String chatName;

    public GetThread(String login, String chatName) {
        this.login = login;
        this.chatName = chatName;
        gson = new GsonBuilder().create();
    }

    public void setMyChatName(String chatName) {
        this.chatName = chatName;
    }

    @Override
    public void run() {
        try {
            while ( ! Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/get?from=" + n);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                try (InputStream is = http.getInputStream()) {
                    byte[] buf = requestBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            if (m.getTo().equals("all") || m.getTo().equals(login) || m.getFrom().equals(login) ||
                                    m.getTo().equals(chatName)) {
                                System.out.println(m);
                                n++;
                            }
                        }
                    }
                }

                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}