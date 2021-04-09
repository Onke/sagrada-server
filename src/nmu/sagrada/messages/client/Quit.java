package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.messages.Message;
import nmu.sagrada.messages.server.Left;

public class Quit  extends Message<ClientHandler>  {
    private static final long serialVersionUID = 103L;

    @Override
    public void apply(ClientHandler client) {
        client.send(new Left());
    }
}
