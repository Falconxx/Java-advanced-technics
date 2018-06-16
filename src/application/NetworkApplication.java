package application;

import connection.*;
import data.mySOAP;

import javax.xml.soap.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkApplication {

    private String myName;
    private int receivePort;
    private int sendPort;
    private ArrayList<Integer> outReceivePort = null;
    private ArrayList<Integer> outSendPort = null;

    private networkReceiver receiver=null;
    private networkSender sender=null;
    private ArrayList<networkReceiver> outReceiver=null;
    private ArrayList<networkSender> outSender=null;
    private URL url;

    public static void main(String args[]){

        try {
            new NetworkApplication(args);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void rerouteMessage(int port, char layer,SOAPMessage message){

        if(outSender != null){
            if(layer == myName.charAt(1)){
                sender.addMessage(message);
            }else {
                for (networkSender sender:outSender) {
                    if(outReceivePort.get(outSender.indexOf(sender))!=port){
                        sender.addMessage(message);
                    }
                }
            }
        }else {
            sender.addMessage(message);
        }
    }

    NetworkApplication(String args[]) throws MalformedURLException {

        if (args.length < 4)
        {
            System.out.println(" Not enough arguments ");
            return;
        }
        myName = args[0];
        receivePort =Integer.parseInt(args[2]);
        sendPort = Integer.parseInt(args[3]);
        url = new URL(args[1]);



        sender = new networkSender(sendPort);
        sender.start();

        receiver = new networkReceiver(this,receivePort,url.getHost(),myName);
        receiver.start();


        if (args.length>4) {
            System.out.println("-> Super knot <");
            outReceivePort = new ArrayList<>();
            outSendPort = new ArrayList<>();
            outReceiver = new ArrayList<>();
            outSender = new ArrayList<>();

            int x = 4;
            while (args.length > x && args[x] != null && args[x + 1] != null) {
                int oRP = Integer.parseInt(args[x]);
                int oSP = Integer.parseInt(args[x+1]);
                networkSender nS = new networkSender(oSP);
                networkReceiver nR = new networkReceiver(this, oRP, url.getHost(), myName);

                outReceivePort.add(oRP);
                outSendPort.add(oSP);
                outReceiver.add(nR);
                outSender.add(nS);

                nS.start();
                nR.start();

                x = x + 2;
            }
        }


        Scanner scan = new Scanner(System.in);

        String message;
        SOAPMessage toSend;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(myName + ", message format [receiver]:[message], -exit to quit");
            message = scan.nextLine();
            if (message != null) {
                if(message.compareTo("-exit") == 0){
                    break;
                }
                try {
                    String[] messageElem = message.split(":");
                    if(messageElem[0].length()==2) {

                        toSend = mySOAP.createMessage(myName, messageElem[0], messageElem[1]);
                        if (outSender != null) {
                            if (messageElem[0].charAt(1) == myName.charAt(1)) {
                                sender.addMessage(toSend);
                            } else {
                                for (networkSender sender:outSender) {
                                    sender.addMessage(toSend);
                                }
                            }

                        } else {
                            sender.addMessage(toSend);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("-> shutting down application");
        sender.shutDown();
        receiver.shutDown();

        try {

            sender.join();
            receiver.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
