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

public class TCPClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1111); // Assuming server is running on localhost with port 1111
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Get member details from user
                System.out.println("Enter member details:");
                String details = inFromUser.readLine();

                // Send member details to server
                outToServer.writeUTF(details);
                outToServer.flush();

                // Receive feedback from server
                DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
                String feedback = inFromServer.readUTF();
                System.out.println("Server: " + feedback);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}