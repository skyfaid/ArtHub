package entities;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import controllers.Reclamations.Client;

public class Clientclass {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public Clientclass(Socket socket) throws IOException {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            System.out.println("error creating client");
            e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }
    public void sendMessageToServer(String messageToServer) throws IOException {
        try{
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("error sending message to the client");
            closeEverything(socket, bufferedReader,bufferedWriter);
        }
    }
    public void receiveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try{
                        String messageFromServer=bufferedReader.readLine();
                        controllers.Reclamations.Server.addLabel(messageFromServer,vBox);
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