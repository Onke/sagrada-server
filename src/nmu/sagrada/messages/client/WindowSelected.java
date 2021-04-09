package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.Game;
import nmu.sagrada.messages.Message;

public class WindowSelected extends Message<ClientHandler> {
    private static final long serialVersionUID = 105L;
    public String window;

    public WindowSelected(String window) {
        this.window = window;
    }

    @Override
    public void apply(ClientHandler client) {
        Game.windowSelection(client, window);
    }
}
