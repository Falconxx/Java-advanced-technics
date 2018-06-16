package connection;

import application.NetworkApplication;

import javax.xml.soap.*;

import java.io.IOException;
import java.io.InputStream;

import java.net.Socket;

import java.util.Iterator;

public class networkReceiver  extends Thread{

    private int port;
    private String host;
    private String myName;

    private MessageFactory factory;
    private Socket mySocket;
    private boolean appRunning =true;
    private NetworkApplication parent;

    @Override
    public void run(){
        System.out.println("-> receiver is up and running");
        while (appRunning) {
            try {

                ReceiveMessage();
                reconnectSocket();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SOAPException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-> shutting down receiver");
    }

    public void shutDown(){
        appRunning=false;
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public networkReceiver(NetworkApplication parent, int port, String host, String myName){

        while (mySocket == null) {
            System.out.println("-> setting up receiver");
            this.parent = parent;
            this.port = port;
            this.host = host;
            this.myName = myName;
            try {

                factory = MessageFactory.newInstance();
                mySocket = new Socket(host, port);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SOAPException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void reconnectSocket() throws IOException {
        mySocket.close();
        mySocket = new Socket(host, port);
    }

    private void ReceiveMessage() throws IOException, SOAPException {

        InputStream messageInput = mySocket.getInputStream();

        MimeHeaders myHeader = new MimeHeaders();
        myHeader.addHeader("receiver", myName);
        SOAPMessage getMessage = factory.createMessage(myHeader, messageInput);

        Iterator<SOAPElement> elements =  getMessage.getSOAPPart().getEnvelope().getHeader().getChildElements();

        String receiver=elements.next().getValue();
        SOAPElement TTLElement = elements.next();
        int TTL = Integer.parseInt( TTLElement.getValue())+1 ;

        TTLElement.setValue(  String.valueOf( TTL ) );

        if(receiver.compareTo(myName) != 0){
            if(TTL > 10){
                System.out.println(receiver + " TTL to high, removing");
                return;
            }

            System.out.println("\n");
            System.out.println(receiver + " not me, passing forward");
            System.out.println(TTLElement.getValue() + " TTL");

            parent.rerouteMessage(port, receiver.charAt(1), getMessage);
            return;
        }

        elements = getMessage.getSOAPPart().getEnvelope().getBody().getChildElements();

        String message = elements.next().getValue();
        String from = elements.next().getValue();

        System.out.println("\n"+from+": "+message);

        System.out.println();

        return;
    }

}
