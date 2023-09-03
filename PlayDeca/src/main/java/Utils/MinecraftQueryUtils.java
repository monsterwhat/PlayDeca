package Utils;

import jakarta.inject.Named;
import java.net.*;
import java.util.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Named
public class MinecraftQueryUtils implements Serializable{
    
    private InetSocketAddress address;
    private Map<String, String> values;
    private InetSocketAddress queryAddress;
    private String[] pluginsArray;
    private String ServerName;
    
    public MinecraftQueryUtils() {
    }
    
    // Constructor for creating a MinecraftQuery object with host, port, and queryPort
    public MinecraftQueryUtils(String host, int port, int queryPort) {
        this(new InetSocketAddress(host, queryPort), new InetSocketAddress(host, port));
    }

    // Constructor for creating a MinecraftQuery object with queryAddress and address
    public MinecraftQueryUtils(InetSocketAddress queryAddress, InetSocketAddress address) {
        this.address = address;
        this.queryAddress = (queryAddress.getPort() == 0) ? new InetSocketAddress(queryAddress.getHostName(), address.getPort()) : queryAddress;
    }

    //Pings the server using JakartaEE
    public boolean pingServer() {
        String service = "_minecraft._tcp." + address.getHostName();
        try {
            InetAddress[] addresses = InetAddress.getAllByName(service);
            if (addresses != null && addresses.length > 0) {
                for (InetAddress addr : addresses) {
                    String host = addr.getHostAddress();
                    int srvPort = address.getPort();
                    address = new InetSocketAddress(host, srvPort);
                    queryAddress = new InetSocketAddress(host, srvPort);
                }
            }
        } catch (UnknownHostException ignored) {
        }
        try (Socket socket = new Socket()) {
            socket.connect(address, 1500);
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    // Sends a query to the server
    public void sendQuery() throws IOException {
        sendQueryRequest();
    }

    // Returns the map of values received from the query
    public Map<String, String> getValues() {
        if (values == null) {
            throw new IllegalStateException("Query has not been sent yet!");
        }
        return values;
    }

    // Sends a query request and processes the received data
    private void sendQueryRequest() throws IOException {
        InetSocketAddress local = (queryAddress.getPort() == 0) ? new InetSocketAddress(queryAddress.getAddress(), address.getPort()) : queryAddress;
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
           
            // Send a challenge packet to the server
            sendPacket(socket, local, (byte) 0xFE, (byte) 0xFD, (byte) 0x09, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01);
            int challengeInteger = parseChallengeInteger(receivePacket(socket, new byte[10240]));

            // Send the final query packet to the server
            sendPacket(socket, local, (byte) 0xFE, (byte) 0xFD, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) (challengeInteger >> 24), (byte) (challengeInteger >> 16), (byte) (challengeInteger >> 8), (byte) challengeInteger, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00);

            byte[] receiveData = new byte[10240];
            int length = receivePacket(socket, receiveData).getLength();
            values = new HashMap<>();
            AtomicInteger cursor = new AtomicInteger(5);

            // Read the received data and populate the values map
            while (cursor.get() < length) {
                String key = readString(receiveData, cursor);
                if (key.isEmpty()) {
                    break;
                } else {
                    String value = readString(receiveData, cursor);
                    values.put(key, value);
                }
            }
        }
    }
    
    // Helper method to send a packet to the server
    private static void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, byte... data) throws IOException {
        socket.send(new DatagramPacket(data, data.length, targetAddress.getAddress(), targetAddress.getPort()));
    }

    // Helper method to parse the challenge integer from the packet
    private static int parseChallengeInteger(DatagramPacket packet) {
        byte[] receiveData = packet.getData();
        int i = 0;
        byte[] buffer = new byte[11];
        for (int count = 5; receiveData[count] != 0; count++) {
            buffer[i++] = receiveData[count];
        }
        return Integer.parseInt(new String(buffer).trim());
    }

    // Helper method to receive a packet from the server
    private static DatagramPacket receivePacket(DatagramSocket socket, byte[] buffer) throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        socket.receive(dp);
        return dp;
    }

    // Helper method to read a null-terminated string from a byte array
    private static String readString(byte[] array, AtomicInteger cursor) {
        final int startPosition = cursor.get();
        for (; cursor.get() < array.length && array[cursor.get()] != 0; cursor.incrementAndGet());
        int length = cursor.get() - startPosition;
        cursor.incrementAndGet(); // Move cursor past the null terminator
        return new String(array, startPosition, length);
    }
   
    public int getNumPlayers() throws IOException {
        sendQueryRequest();
        String numPlayersStr = values.get("numplayers");
        if (numPlayersStr != null) {
            return Integer.parseInt(numPlayersStr);
        }
        return 0;
    }
    
    // Returns the MOTD (Message of the Day) of the server
    public String getMOTD() throws IOException {
        sendQueryRequest();
        return values.get("hostname");
    }

    public String getVersion() throws IOException {
        sendQueryRequest();
        return values.get("version");
    }
    
    public String getServer() throws IOException {
        sendQueryRequest();
        String pluginsString = values.get("plugins");
        if (pluginsString != null) {
        pluginsArray = pluginsString.split(":");
        ServerName = pluginsArray[0];
        return ServerName;
        }
        return "";
    }

    public String[] getPlugins() throws IOException {
        sendQueryRequest();
        String pluginsString = values.get("plugins");
        if (pluginsString != null) {
        pluginsArray = pluginsString.split(";");
        
        pluginsArray = Arrays.copyOfRange(pluginsArray, 1, pluginsArray.length);
        return pluginsArray;
        }
        return new String[0];
    }

}
