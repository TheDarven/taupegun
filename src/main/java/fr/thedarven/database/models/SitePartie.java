package fr.thedarven.database.models;

public class SitePartie {

    private int id;
    private String type;
    private int duree;
    private long debut;
    private int visible;

    public SitePartie(int id, String type, int duree, long debut, int visible) {
        this.id = id;
        this.type = type;
        this.duree = duree;
        this.debut = debut;
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public long getDebut() {
        return debut;
    }

    public void setDebut(long debut) {
        this.debut = debut;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
