/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass1.adsassignment1;

/**
 *
 * @author jacks 01/04/2024 UDPServer.java
 *
 */
import java.io.*;
import java.net.*;

public class UDPServer {

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(2237);
            System.out.println("UDPServer: Server started. Waiting for requests...");

            while (true) {
                //Receive request from client
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                //Extract filename
                String fileName = new String(receivePacket.getData(), 0, receivePacket.getLength());

                //Read object from file and send to client
                sendObjectToClient(fileName, receivePacket.getAddress(), receivePacket.getPort(), serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendObjectToClient(String fileName, InetAddress clientAddress, int clientPort, DatagramSocket serverSocket) throws IOException {
        try {
            //Deserialise object from file
            Member member = Member.deserialise(fileName);

            //Convert to bytes
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteStream);
            objectOut.writeObject(member);
            objectOut.flush();
            byte[] sendData = byteStream.toByteArray();
            objectOut.close();

            //Send data to client
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);
        } catch (EOFException e) {
            //Handle EOFException
            System.out.println("Reached end of file while reading object data.");
        } catch (IOException e) {
            //Handle other IOExceptions
            e.printStackTrace();
        }
    }
}
