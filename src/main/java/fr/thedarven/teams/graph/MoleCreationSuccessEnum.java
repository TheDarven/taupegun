package fr.thedarven.teams.graph;

public enum MoleCreationSuccessEnum {
    INCORRECT_MOLE_AMOUNT(false),
    CREATED(true);

    private boolean correct;
    MoleCreationSuccessEnum(boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect() {
        return this.correct;
    }
}
