/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass1.adsassignment1;

/**
 *
 * @author jacks
 */
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1111); // Port for TCP connection
            System.out.println("Server started. Waiting for clients...");

            Timer timer = new Timer();
            timer.schedule(new SaveDataTask(), 0, 2000); // Save data every 2 seconds

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SaveDataTask extends TimerTask {
        public void run() {
            // Code to serialize member data and save to file "memberlistObject"
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

                // Read member details from client
                String details = inFromClient.readLine();
                // Save member details to file "memberlist.txt"
                FileWriter fileWriter = new FileWriter("memberlist.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(details + "\n");
                bufferedWriter.close();

                // Send feedback to client
                outToClient.writeUTF("Member details saved successfully!");
                outToClient.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}