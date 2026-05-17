package core;

/**
 * Enum trieda reprezentujúca typy nepriateľov.
 * @author Samuel Ďuriš
 * @version V3
 */
public enum TypEnemaka {
    HRAC("HRAC"),
    BOT("Bot"),
    BOSS("Boss");

    private String enemak;

    /**
     * Konštruktor - inicializuje typ nepriateľa s jeho názvom.
     */
    TypEnemaka(String enemak) {
        this.enemak = enemak;
    }

    /**
     * Getter pre názov nepriateľa.
     */
    public String getEnemak() {
        return this.enemak;
    }
}
