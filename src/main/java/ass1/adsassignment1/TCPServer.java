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
            serializeAndSaveMemberData();
        }
        
        private void serializeAndSaveMemberData() {
            try {
                // Load member data from "memberlist.txt"
                FileInputStream fileIn = new FileInputStream("memberlist.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileIn));
                String line;
                StringBuilder serializedData = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    serializedData.append(line).append("\n");
                }
                bufferedReader.close();

                // Serialize and save member data
                FileOutputStream fileOut = new FileOutputStream("memberlistObject");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(serializedData.toString());
                out.close();
                fileOut.close();
                System.out.println("Member data serialized and saved as memberlistObject.");
            } catch (IOException e) {
                e.printStackTrace();
            }
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