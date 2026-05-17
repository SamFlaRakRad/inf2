package players;

import core.HernyObjekt;
import java.awt.Image;

/**
 * Abstraktná trieda Ai - predstavuje umelú inteligenciu pre AI nepriateľov
 * AI používa mapu na orientáciu a BFS algoritmus na nájdenie najkratšej cesty k hráčovi.
 * @author Samuel Ďuriš
 * @version V3
 */
public abstract class Ai extends HernyObjekt implements Postava {

    private String[] mapa;
    private int pocetR;
    private int pocetS;
    private int velkostS;
    private int pohybX = 0;
    private int pohybY = 0;

    /**
     * Konštruktor - inicializuje AI s obrázkom a pozíciou.
     */
    public Ai(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost, velkost);
    }

    /**
     * Nastaví mapu, ktorú bude AI používať na pathfinding.
     */
    public void nastavMapu(String[] mapa, int velkostS) {
        this.mapa = mapa;
        this.pocetR = mapa.length;
        this.pocetS = mapa[0].length();
        this.velkostS = velkostS;
    }

    /**
    * Metoda nastavuje ciel ktory bude ai nasledovat
    */
    public abstract void sledujCiel(Hrac ciel);

    /**
     * Getter pre obrazok vlavo
     */
    public abstract Image getVlavo();

    /**
     * Getter pre obrazok pravo
     */
    public abstract Image getVpravo();
    /**
     * Getter pre mapu.
     */

    public String[] getMapa() {
        return this.mapa;
    }

    /**
     * Getter pre veľkosť politička.
     */
    public int getVelkostS() {
        return this.velkostS;
    }

    /**
     * Getter pre pohybový vektor v smere X.
     */
    public int getPohybX() {
        return this.pohybX;
    }

    /**
     * Getter pre pohybový vektor v smere Y.
     */
    public int getPohybY() {
        return this.pohybY;
    }

    /**
     * Koriguje pohyb na základe nájdeného smeru z pathfindingu.
     */
    public void korekcia(int[] smer, int x, int y, int rychlost) {
        if (smer[0] != 0) {
            this.pohybX = smer[0] * rychlost;
            int cielY = y * this.getVelkostS();
            int rozdielY = cielY - this.getY();
            this.pohybY = Integer.signum(rozdielY) * Math.min(rychlost, Math.abs(rozdielY));
        } else if (smer[1] != 0) {
            this.pohybY = smer[1] * rychlost;
            int cielX = x * this.getVelkostS();
            int rozdielX = cielX - this.getX();
            this.pohybX = Integer.signum(rozdielX) * Math.min(rychlost, Math.abs(rozdielX));
        } else {
            this.pohybX = 0;
            this.pohybY = 0;
        }
    }

    /**
     * Pathfinding metóda - nájde smer k cieľu s predikciou jeho pohybu.
     */
    public int[] pathfinding(int posX, int posY, int cielX, int cielY, int cielPohybX, int cielPohybY) {
        int offset = 3;
        int x = cielX;
        int y = cielY;

        // Predikcia pohybu
        if (cielPohybX > 0) {
            x = Math.min(this.pocetS - 1, cielX + offset);
        } else if (cielPohybX < 0) {
            x = Math.max(0, cielX - offset);
        } else if (cielPohybY > 0) {
            y = Math.min(this.pocetR - 1, cielY + offset);
        } else if (cielPohybY < 0) {
            y = Math.max(0, cielY - offset);
        }

        // Ak predpoveda poziciu stenu, zostane targetovat ciel
        if (this.mapa[y].charAt(x) == 'X') {
            x = cielX;
            y = cielY;
        }

        int[] krok = this.bfsKrok(posX, posY, x, y);
        if (krok != null) {
            return krok;
        } else {
            return new int[]{0, 0};
        }

    }

    /**
     * BFS algoritmus
     * Vracia prvý krok cesty najkratšej cestou.
     */
    public int[] bfsKrok(int startStlpec, int startRiadok, int cielStlpec, int cielRiadok) {
        // Ak sme už na cieľovej pozícií
        if (startStlpec == cielStlpec && startRiadok == cielRiadok) {
            return new int[]{0, 0};
        }

        int size = this.pocetS * this.pocetR;
        int[] dist = new int[size];
        int[] rodicStlpec = new int[size];
        int[] rodicRiadok = new int[size];

        // Inicializácia polí
        for (int i = 0; i < size; i++) {
            dist[i] = -1;
            rodicStlpec[i] = -1;
            rodicRiadok[i] = -1;
        }

        int[] frontaStlpec = new int[size];
        int[] frontaRiadok = new int[size];
        int start = 0;
        int konec = 0;

        // BFS inicializácia
        int startIdx = startRiadok * this.pocetS + startStlpec;
        dist[startIdx] = 0;
        rodicStlpec[startIdx] = startStlpec;
        rodicRiadok[startIdx] = startRiadok;

        frontaStlpec[konec] = startStlpec;
        frontaRiadok[konec] = startRiadok;
        konec++;

        // Smery pohybu: T, D, L, R
        int[] deltaStlpec = {0, 0, -1, 1};
        int[] deltaRiadok = {-1, 1, 0, 0};

        boolean nasiel = false;

        // BFS hlavná slučka
        while (start < konec && !nasiel) {
            int aktStlpec = frontaStlpec[start];
            int aktRiadok = frontaRiadok[start];
            start++;

            for (int d = 0; d < 4; d++) {
                int novyStlpec = aktStlpec + deltaStlpec[d];
                int novyRiadok = aktRiadok + deltaRiadok[d];

                // Hraničná kontrola
                if (novyStlpec < 0 || novyStlpec >= this.pocetS || novyRiadok < 0 || novyRiadok >= this.pocetR) {
                    continue;
                }

                // Kontrola steny
                if (this.mapa[novyRiadok].charAt(novyStlpec) == 'X') {
                    continue;
                }

                int idx = novyRiadok * this.pocetS + novyStlpec;

                // Ak sme už navštívili toto políčko
                if (dist[idx] != -1) {
                    continue;
                }

                // Aktualizácia vzdialenosti a rodiča
                dist[idx] = dist[aktRiadok * this.pocetS + aktStlpec] + 1;
                rodicStlpec[idx] = aktStlpec;
                rodicRiadok[idx] = aktRiadok;

                // Skontroluj, či sme dosiahli cieľ
                if (novyStlpec == cielStlpec && novyRiadok == cielRiadok) {
                    nasiel = true;
                    break;
                }

                // Pridaj do fronty
                frontaStlpec[konec] = novyStlpec;
                frontaRiadok[konec] = novyRiadok;
                konec++;
            }
        }

        // Ak sme nenašli cestu k cieľu
        if (!nasiel) {
            return null;
        }

        // Rekonštrukcia prvého kroku cesty
        int aktStlpec = cielStlpec;
        int aktRiadok = cielRiadok;
        while (true) {
            int predStlpec = rodicStlpec[aktRiadok * this.pocetS + aktStlpec];
            int predRiadok = rodicRiadok[aktRiadok * this.pocetS + aktStlpec];

            // Ak sme na kroku bezprostredne za počiatkom
            if (predStlpec == startStlpec && predRiadok == startRiadok) {
                break;
            }

            aktStlpec = predStlpec;
            aktRiadok = predRiadok;
        }

        return new int[]{aktStlpec - startStlpec, aktRiadok - startRiadok};
    }
}