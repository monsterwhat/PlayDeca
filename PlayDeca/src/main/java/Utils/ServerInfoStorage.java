package Utils;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Al
 */

@Named
public class ServerInfoStorage implements Serializable{
    
    private List<ServerInfo> serverInfoHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 30; // Maximum number of records to store

    @Inject private MinecraftQueryUtils minecraftQuery;
    
    @PostConstruct
    public void init(){
        minecraftQuery = new MinecraftQueryUtils("playdeca.com", 25565, 0);
    }
    
    public ServerInfoStorage() {
    }
    
    public void pingAndStoreServerInfo() {
        try {
            if (minecraftQuery.pingServer()) {
                ServerInfo serverInfo = new ServerInfo(
                    minecraftQuery.getNumPlayers(),
                    minecraftQuery.getMOTD(),
                    minecraftQuery.getVersion(),
                    minecraftQuery.getServer(),
                    minecraftQuery.getPlugins()
                );

                serverInfoHistory.add(serverInfo);

                if (serverInfoHistory.size() > MAX_HISTORY_SIZE) {
                    serverInfoHistory.remove(0); // Remove the oldest record if limit exceeded
                }

            } else {
                System.out.println("Server is offline.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ServerInfo> getServerInfoHistory() {
        if(serverInfoHistory == null){
            if(minecraftQuery.pingServer()){
                return serverInfoHistory;
            }
        }
        return null;
    }
}
