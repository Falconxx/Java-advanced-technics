package data;

import javax.xml.soap.*;

public class mySOAP {

    public static SOAPMessage createMessage(String from, String to,String message) throws SOAPException  {

        /* Soap */
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage myMessage = factory.createMessage();
        SOAPPart part = myMessage.getSOAPPart();

        SOAPEnvelope envelope = part.getEnvelope();
        SOAPHeader header = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        SOAPHeaderElement receiver = header.addHeaderElement(envelope.createName("receiver",to,"http://localhost"));
        receiver.addTextNode(to);
        SOAPHeaderElement TTL = header.addHeaderElement(envelope.createName("yyl",to,"http://localhost"));
        TTL.addTextNode("0");

        SOAPBodyElement messageElement = body.addBodyElement(envelope.createName("message", to, "http://localhost"));
        messageElement.addTextNode(message);
        SOAPBodyElement fromElement = body.addBodyElement(envelope.createName("from", to, "http://localhost"));
        fromElement.addTextNode(from);

        /* Soap */

        return myMessage;
    }
}
