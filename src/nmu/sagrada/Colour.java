package nmu.sagrada;

public enum Colour {
    BLUE, GREEN, PURPLE, RED, YELLOW;

    public char asChar(){
        switch (this){
            case BLUE:
                return 'b';
            case GREEN:
                return 'g';
            case PURPLE:
                return 'p';
            case RED:
                return 'r';
            case YELLOW:
                return 'y';
        }
        return ' ';
    }

}
