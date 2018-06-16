package connection;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class networkSender extends Thread{

    private ServerSocket mySocket;
    private SOAPMessage message = null;
    private boolean appRunning =true;

    @Override
    public void run(){
        System.out.println("-> sender is up and running");

        while (appRunning){

            if(message != null) {
                try {

                    sendMessage();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SOAPException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-> shutting down sender");


    }

    public void shutDown(){
        appRunning=false;
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public networkSender(int port){
        System.out.println("-> setting up sender");

        while (mySocket == null) {
            try {
                mySocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessage(SOAPMessage message){
        this.message = message;
    }

    private void sendMessage() throws IOException, SOAPException {

        System.out.println("-> sending message");
        Socket soc = mySocket.accept();
        PrintStream sendStream = new PrintStream(soc.getOutputStream());
        message.writeTo(sendStream);
        sendStream.flush();
        soc.close();
        message = null;

    }

}
