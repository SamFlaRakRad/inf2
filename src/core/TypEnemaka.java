package core;

public enum TypEnemaka {
    HRAC("HRAC"),
    BOT("Bot"),
    BOSS("Boss");

    private String enemak;

    TypEnemaka(String enemak) {
        this.enemak = enemak;
    }
    public String getEnemak() {
        return this.enemak;
    }
}
