package Core;

/**
 * Enum trieda v ktorej vyberám pre-sety hry 
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public enum Obtiaznost {
    LAHKA("Ľahká"),
    STREDNA("Stredná"),
    TAZKA("Ťažká");
    
    private String nazov;
    
    /**
     * Konštruktor
     */
    Obtiaznost(String nazov) {
        this.nazov = nazov;
    }
    
    /**
     * getter jednotlivy pre-set
     */
    public String getNazov() {
        return this.nazov;
    }

}
