package net;

import greet.Greetable;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class Server {

    private static Map<String, Greetable> loadHandlers() {
        Map<String, Greetable> result = new HashMap<>();

            try(InputStream is = Server.class.getClassLoader().getResourceAsStream("server.properties")){

                Properties properties = new Properties();
                properties.load(is);

                for (Object command : properties.keySet()){

                    String className = properties.getProperty(command.toString());
                    Class<Greetable> cl = (Class<Greetable>) Class.forName(className);
                    Greetable handler = cl.getConstructor().newInstance();
                    result.put(command.toString(), handler);

                }
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        return result;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Map<String, Greetable> handlers = loadHandlers();
        ServerSocket socket = new ServerSocket(4444, 2000);
        System.out.println("Server started.");
        while (true) {
            new SimpleServer(socket.accept(), handlers).start();
        }
    }
}


class SimpleServer extends Thread {

    private Socket client;
    private Map<String, Greetable> handlers;

    public SimpleServer(Socket client, Map<String, Greetable> handlers) {
        this.client = client;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        handleRequest(client);
    }

    private void handleRequest(Socket client) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            String request = reader.readLine();
            String[] lines = request.split("\\s+");
            String userName = lines[1];
            String command = lines[0];
            String response = responseBuilder(command, userName);

            writer.write(response);
            writer.newLine();
            writer.flush();

            reader.close();
            writer.close();

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String responseBuilder(String command, String name) {
        Greetable handler = handlers.get(command);
        if(handler != null){
            return handler.buildResponse(name);
        }
        return "Hello, " + name;
    }
}
