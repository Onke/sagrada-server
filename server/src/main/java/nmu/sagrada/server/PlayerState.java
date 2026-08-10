package nmu.sagrada.server;

import nmu.sagrada.core.Colour;
import nmu.sagrada.core.WindowCard;

import java.util.ArrayList;
import java.util.List;

final class PlayerState {
    final String id;
    final String name;
    final boolean bot;
    Colour privateObjective;
    List<String> windowChoices = new ArrayList<>();
    String windowId;
    WindowCard window;

    PlayerState(String id, String name, boolean bot) {
        this.id = id;
        this.name = name;
        this.bot = bot;
    }

    boolean isReady() { return window != null; }
}
