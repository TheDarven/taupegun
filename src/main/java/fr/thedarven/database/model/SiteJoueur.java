package fr.thedarven.database.model;

public class SiteJoueur {

    private String uuid;
    private String pseudo;
    private long last;
    private int time_play;

    public SiteJoueur(String uuid, String pseudo, long last, int time_play) {
        this.uuid = uuid;
        this.pseudo = pseudo;
        this.last = last;
        this.time_play = time_play;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public int getTime_play() {
        return time_play;
    }

    public void setTime_play(int time_play) {
        this.time_play = time_play;
    }
}
