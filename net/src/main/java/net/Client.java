package net;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class Client {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 5; i++) {
            SimpleClient sc = new SimpleClient(i);
            sc.start();
        }
    }


}

class SimpleClient extends Thread {
    private static final String COMMAND[] = {"MORNING", "EVENING", "TEST"};
    private int cmndNumber;

    public SimpleClient(int cmndNumbre){
        this.cmndNumber = cmndNumbre;
    }
    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 4444);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String command = COMMAND[cmndNumber % COMMAND.length];
            String sb = command + " " + "Vasya";
            writer.write(sb);
            writer.newLine();
            writer.flush();

            String answer = reader.readLine();
            System.out.println(answer);

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
