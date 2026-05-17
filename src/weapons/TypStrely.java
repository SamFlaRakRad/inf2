package weapons;

/**
 * Enum trieda reprezentujúca typy striel.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public enum TypStrely {
    NORMALNA("Normalna"),
    SHOTGUN("Shotgun");

    private String typ;

    /**
     * Konštruktor - inicializuje typ strely s jeho názvom.
     */
    TypStrely(String typ) {
        this.typ = typ;
    }

    /**
     * Getter pre názov typu strely.
     */
    public String getNazov() {
        return this.typ;
    }
}
