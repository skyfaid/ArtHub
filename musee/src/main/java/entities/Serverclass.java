package entities;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Serverclass {
    private ServerSocket serverSocket; //serversocket ki touslelha connection it creates a socket to comunicate who ever connected //
    private Socket socket;// allows comunication throught streams
    private BufferedReader bufferedReader; //read message from client
    private BufferedWriter bufferedWriter;

    public Serverclass(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();// blocker method (wait for client to connect first )
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//inputstream to read content to who we are connected to
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  //outputstream for writing content to who we are connected to

        } catch (IOException e) {
            System.out.println("error creating server");
            e.printStackTrace();
        }
    }



    //method to send message to client
    public void sendMessageToClient(String messageToClient) throws IOException {
        try{
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("error sending message to the client");
            closeEverything(socket, bufferedReader,bufferedWriter);
        }
    }
    //method to receive messages from client

    public void receiveMessageFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try{
                        String messageFromClient=bufferedReader.readLine();
                        controllers.Reclamations.Server.addLabel(messageFromClient,vBox);
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("error receiving message from the client");
                        try {
                            closeEverything(socket,bufferedReader,bufferedWriter);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }

                }
            }
        }).start(); //execute thread
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter) throws IOException {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch(IOException e){
            e.printStackTrace(); }

    }
}
