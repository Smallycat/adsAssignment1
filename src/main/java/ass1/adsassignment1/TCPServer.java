/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass1.adsassignment1;

/**
 *
 * @author jacks 01/04/2024 TCPServer.java
 *
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TCPServer {

    public static void main(String[] args) {
        //Ensure correct directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        //Start server
        try (ServerSocket serverSocket = new ServerSocket(1137)) {
            System.out.println("Server started. Waiting for clients...");

            //Timer for periodic data saving
            Timer timer = new Timer();
            timer.schedule(new SaveDataTask(), 0, 2000);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Server: Client connected. Creating new thread to handle client request...");

                    //Create new thread
                    new Thread(new ClientHandler(clientSocket)).start();

                    System.out.println("Server: Thread started to handle client request.");
                } catch (SocketException e) {

                    System.out.println("Server: Client disconnected.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SaveDataTask extends TimerTask {

        public void run() {
            //Serialise member data and save
            serialiseAndSaveMemberData();
        }

        private void serialiseAndSaveMemberData() {
            try {
                File file = new File("memberlist.txt");
                if (!file.exists() || file.length() == 0) {
                    System.out.println("Server: memberlist.txt is empty or does not exist.");
                    return;
                }

                //Load member data from file
                try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

                    // Deserialise Member objects from the file
                    List<Member> members = new ArrayList<>();
                    while (true) {
                        try {
                            Object obj = objectIn.readObject();
                            if (obj instanceof Member) {
                                members.add((Member) obj);
                            }
                        } catch (EOFException e) {
                            break;
                        }
                    }

                    //Serialise and save data
                    try (FileOutputStream fileOut = new FileOutputStream("memberlistObject"); ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
                        for (Member member : members) {
                            objectOut.writeObject(member);
                        }
                    }
                }
                System.out.println("Member data serialised and saved as memberlistObject.");
            } catch (FileNotFoundException e) {
                System.out.println("Server: memberlist.txt does not exist.");
            } catch (IOException | ClassNotFoundException e) {
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
            try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream())) {

                //Read data only when available
                String line;
                if ((line = inFromClient.readLine()) != null) {
                    String details = line;
                    if (!details.isEmpty()) {
                        //Split member details into components
                        String[] memberDetails = details.split(",");

                        String firstName = memberDetails[0];
                        String lastName = memberDetails[1];
                        String address = memberDetails[2];
                        String phoneNumber = memberDetails[3];

                        //Save details to txt file
                        FileWriter fileWriter = new FileWriter("memberlist.txt", true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write("First Name: " + firstName + "\n");
                        bufferedWriter.write("Last Name: " + lastName + "\n");
                        bufferedWriter.write("Address: " + address + "\n");
                        bufferedWriter.write("Phone Number: " + phoneNumber + "\n");
                        bufferedWriter.close();

                        //Debug
                        System.out.println("Server: Received data from client.");
                        System.out.println("Server: Member details saved to memberlist.txt.");

                        //Send feedback to client
                        outToClient.writeUTF("Member details saved successfully!");
                        outToClient.flush();

                        //Debug
                        System.out.println("Server: Response sent to client.");
                    } else {
                        System.out.println("Server: No data received from the client.");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
