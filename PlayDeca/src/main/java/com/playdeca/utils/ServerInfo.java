package com.playdeca.utils;

/**
 *
 * @author Al
 */
public record ServerInfo(int onlinePlayers, String motd, String version, String server, String[] plugins) {
}
