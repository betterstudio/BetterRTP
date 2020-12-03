package fr.better.rtp.entity;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class License {

    private final String key;
    private final String plugin;
    private final String serverIP;

    public License(String key, String plugin, String serverIP) {
        this.key = key;
        this.plugin = plugin;
        this.serverIP = serverIP;
    }

    public void verify(Action actionOnFalse, int secondsCooldown, JavaPlugin instance){
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable() {
            @Override
            public void run() {
                try {
                    if(!request())
                    actionOnFalse.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0,secondsCooldown*20);
    }

    private boolean request() throws IOException {
        String url = "http://" + serverIP + "/verifyKey?pluginKey=" + plugin + "&key=" + key;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

        httpClient.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            System.out.println(response.toString());
            return response.toString().equalsIgnoreCase("true");
        }
    }

    public interface Action{ void execute(); }
}
