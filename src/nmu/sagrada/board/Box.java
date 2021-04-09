package nmu.sagrada.board;

import nmu.sagrada.Die;

public abstract class Box {
    private Die die;

    public Die getDie() {
        return die;
    }

    public void placeDie(Die die) {
        this.die = die;
    }

    public Die removeDie() {
        Die removedDie = this.die;
        this.die = null;
        return removedDie;
    }

    public boolean isEmpty() {
        return die == null;
    }
}
