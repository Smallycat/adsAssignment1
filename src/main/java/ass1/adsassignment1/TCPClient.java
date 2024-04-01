/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass1.adsassignment1;

/**
 *
 * @author jacks 01/04/2024 TCPClient.java
 *
 */
import java.io.*;
import java.net.*;

public class TCPClient {

    public static void main(String[] args) {
        try {
            System.out.println("Client: Attempting to connect to the server...");
            Socket socket = new Socket("localhost", 1137);
            System.out.println("Client: Connection established with the server.");
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            //Input user details
            System.out.println("Enter first name:");
            String firstName = inFromUser.readLine();

            System.out.println("Enter last name:");
            String lastName = inFromUser.readLine();

            System.out.println("Enter home address:");
            String address = inFromUser.readLine();

            System.out.println("Enter phone number:");
            String phoneNumber = inFromUser.readLine();

            //Combine details into one string
            String details = firstName + "," + lastName + "," + address + "," + phoneNumber;

            //Send newly combined details to TCPServer
            outToServer.writeUTF(details);
            outToServer.flush();

            //Notify user that information was sent
            System.out.println("Client: Information sent to the server.");

            //Extra delay to ensure server has set up socket
            Thread.sleep(1000);

            //Display feedback from server
            DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
            String feedback = inFromServer.readUTF();
            System.out.println("Server: " + feedback);

            //Close socket after feedback
            socket.close();
        } catch (IOException | InterruptedException e) {
            System.err.println("Client: Error occurred while connecting to the server or communicating with it.");
            e.printStackTrace();
        }
    }
}
