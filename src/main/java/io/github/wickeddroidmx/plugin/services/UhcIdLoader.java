package io.github.wickeddroidmx.plugin.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UhcIdLoader {

    public int getID() {
        try {
            int id = getRootObject("https://agus5534.tech/hd-uhcid.json").get("id").getAsInt();
            String date = getRootObject("https://agus5534.tech/hd-uhcid.json").get("last_update").getAsString();
            String s = "Se ha encontrado el UHC ID %s, actualizado por última vez: %s";

            Bukkit.getLogger().info(String.format(s,id,date));
            Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "&6"+ String.format(s,id,date)));

            return id;
        } catch (Exception e) {
            Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "&4Se ha encontrado el UHC ID de emergencia, la petición ha derivado en un error"));

            Bukkit.getLogger().severe("Se ha encontrado el UHC ID de emergencia, la petición ha derivado en un error");

            return 253;
        }
    }


    private final JsonObject getRootObject(String url) throws Exception{
        URL link = new URL(url);
        URLConnection request = link.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        return root.getAsJsonObject();
    }
}
