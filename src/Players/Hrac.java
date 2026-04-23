package Players;

/**
 * Táto trieda vytvára hráčov.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Hrac {
    private char smerObr; 
    private int pohybX = 0;
    private int pohybY = 0;
    private boolean vystrelenaStrela = false;

    private final int rychlost = 8; 

    private final int klavesHore;
    private final int klavesDole;
    private final int klavesVlavo;
    private final int klavesVpravo;
    private final int klavesStrelba;
    /**
     * Konštruktor triedy Hrac
     */
    public Hrac(char pociatocnySmer, int hore, int dole, int vlavo, int vpravo, int strelba) {
        this.smerObr = pociatocnySmer;
        this.klavesHore = hore;
        this.klavesDole = dole;
        this.klavesVlavo = vlavo;
        this.klavesVpravo = vpravo;
        this.klavesStrelba = strelba;
    }
    
    /**
     * metoda spracovania pohybu
     */
    public void spracujPohyb(boolean[] stlaceneKlavesy) {
        this.pohybX = 0;
        this.pohybY = 0;

        if (stlaceneKlavesy[this.klavesHore]) {
            this.pohybY = -this.rychlost;
        }
        if (stlaceneKlavesy[this.klavesDole]) {
            this.pohybY = this.rychlost;
        }
        if (stlaceneKlavesy[this.klavesVlavo]) {
            this.pohybX = -this.rychlost;
            this.smerObr = 'L';
        }
        if (stlaceneKlavesy[this.klavesVpravo]) {
            this.pohybX = this.rychlost;
            this.smerObr = 'R';
        }
    }

    /**
     * metoda ktorá oznamuje že hrac vystrelil strelu
     */
    public void vystrel() {
        this.vystrelenaStrela = true;
    }

    /**
     * metoda ktorá po vystreleni vráti povodny nevystrelený stav hráčovy 
     */
    public void resetVystrelena() {
        this.vystrelenaStrela = false;
    }

    /**
     * geter pre pohyb x-ovej osi
     */
    public int getPohybX() { 
        return this.pohybX;
    }
    
    /**
     * geter pre pohyb y-ovej osi
     */
    public int getPohybY() { 
        return this.pohybY; 
    }
    
    /**
     * seter pre pohyb x-ovej osi
     */
    public void setX(int x) {
        this.pohybX = x;
    }
    
    /**
     * seter pre pohyb y-ovej osi
     */
    public void setY(int y) {
        this.pohybY = y;
    }
    
    /**
     * getter pre zaciatocny smer hracov
     */
    public char getSmerObr() { 
        return this.smerObr;
    }
    
    /**
     * getter pre stav hraca 
     */
    public boolean getVystrelena() { 
        return this.vystrelenaStrela;
    }

    /**
     * setter pre zaciatocny smer hracov
     */
    public void setSmerObr(char smer) { 
        this.smerObr = smer; 
    }
}
