package core;

/**
 * Enum trieda reprezentujúca typy herných režimov.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public enum TypHry {
    MULTIPLAYER("Multiplayer"),
    SINGLEPLAYER("Singleplayer");

    private final String typHry;

    /**
     * Konštruktor - inicializuje typ hry s jeho názvom.
     */
    TypHry(String typHry) {
        this.typHry = typHry;
    }

    /**
     * Getter pre názov typu hry.
     */
    public String getTypHry() {
        return this.typHry;
    }
}
