package fr.thedarven.database.model;

public class SiteTaupe {

    private int id;
    private int id_partie;
    private String uuid;
    private int taupe;
    private int supertaupe;
    private int id_team;
    private int mort;
    private int kills;

    public SiteTaupe(int id, int id_partie, String uuid, int taupe, int supertaupe, int id_team, int mort, int kills) {
        this.id = id;
        this.id_partie = id_partie;
        this.uuid = uuid;
        this.taupe = taupe;
        this.supertaupe = supertaupe;
        this.id_team = id_team;
        this.mort = mort;
        this.kills = kills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_partie() {
        return id_partie;
    }

    public void setId_partie(int id_partie) {
        this.id_partie = id_partie;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getTaupe() {
        return taupe;
    }

    public void setTaupe(int taupe) {
        this.taupe = taupe;
    }

    public int getSupertaupe() {
        return supertaupe;
    }

    public void setSupertaupe(int supertaupe) {
        this.supertaupe = supertaupe;
    }

    public int getId_team() {
        return id_team;
    }

    public void setId_team(int id_team) {
        this.id_team = id_team;
    }

    public int getMort() {
        return mort;
    }

    public void setMort(int mort) {
        this.mort = mort;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}
