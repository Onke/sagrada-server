package nmu.sagrada.board;

import java.util.ArrayList;

import static nmu.sagrada.Colour.*;

public class WindowCard {
    private String windowCardId;
    private ArrayList<WindowCardBox> windowGrid;
    public final int LENGTH = 20;

    public WindowCard(String windowCardId) {
        this.windowCardId = windowCardId;
        windowGrid = new ArrayList<>();

        setupWindow(windowCardId);
    }

    private void setupWindow(String windowCardID) {
        int gridLength = windowCardID.length();
        if (gridLength != LENGTH) {
            System.out.println("Window ID invalid");
            return;
        }

        for (int i = 0; i < gridLength; i++) {
            char windowCardIdChar = windowCardID.charAt(i);

            switch (windowCardIdChar) {
                case 'b':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), BLUE.asChar()));
                    break;
                case 'g':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), GREEN.asChar()));
                    break;
                case 'p':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), PURPLE.asChar()));
                    break;
                case 'r':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), RED.asChar()));
                    break;
                case 'y':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), YELLOW.asChar()));
                    break;
                case '1':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), 1));
                    break;
                case '2':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), 2));
                    break;
                case '3':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), 3));
                    break;
                case '4':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), 4));
                    break;
                case '5':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), 5));
                    break;
                case '6':
                    windowGrid.add(new WindowCardBox("box" + (i + 1), 6));
                    break;
                default:
                    windowGrid.add(new WindowCardBox("box" + (i + 1)));

            }

        }
    }

    //Getters and setters

    public String getWindowCardId() {
        return windowCardId;
    }

    public void setWindowCardId(String windowCardId) {
        this.windowCardId = windowCardId;
    }

    public ArrayList<WindowCardBox> getWindowGrid() {
        return windowGrid;
    }

    public void setWindowGrid(ArrayList<WindowCardBox> windowGrid) {
        this.windowGrid = windowGrid;
    }


}
