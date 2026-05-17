package core;

/**
 * Enum trieda reprezentujúca úrovne obtiažnosti hry.
 * @author Samuel Ďuriš
 * @version V3
 */
public enum Obtiaznost {
    LAHKA("Ľahká"),
    STREDNA("Stredná"),
    TAZKA("Ťažká");
    
    private String nazov;
    
    /**
     * Konštruktor - inicializuje úroveň obtiažnosti s jej názvom.
     */
    Obtiaznost(String nazov) {
        this.nazov = nazov;
    }
    
    /**
     * Getter pre názov úrovne obtiažnosti.
     */
    public String getNazov() {
        return this.nazov;
    }

}
