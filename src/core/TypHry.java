package core;

public enum TypHry {
    MULTIPLAYER("Multiplayer"),
    SINGLEPLAYER("Singleplayer");

    private final String typHry;
    TypHry(String typHry) {
        this.typHry = typHry;
    }
    public String getTypHry() {
        return this.typHry;
    }
}
