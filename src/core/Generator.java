package core;

import java.util.Random;
/**
 * Generátor mapy ktorý vytvorí mapu na zaklade zadanej obtiažnosti.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Generator {
    private static final int RIADKY = 20;
    private static final int STLPCE = 25;
    private Random random;
    private int hrac1X = 1;
    private int hrac1Y = RIADKY / 2;  
    private int hrac2X = STLPCE - 2;
    private int hrac2Y = RIADKY / 2;
    
    /**
     * Konštruktor
     */
    public Generator() {
        this.random = new Random();
    }

    /**
     * Vytvorí mapu podľa obtiažnosti
     */
    public String[] vytvorMapu(Obtiaznost obtiaznost) {
        String[] mapa;
        switch (obtiaznost) {
            case LAHKA:
           // Ľahká - 30% stien, 5 nábojov
                mapa = this.opravenaMapu(0.30, 5);
                break;

            case STREDNA:
            // Stredná - 40% stien, 4 náboje
                mapa = this.opravenaMapu(0.40, 4);
                break;

            case TAZKA:
            // Ťažká - 50% stien, 3 náboje
                mapa = this.opravenaMapu(0.50, 3);
                break;
            default: 
                mapa = this.opravenaMapu(0, 0);
                break;
        }
        return mapa;
    }

    /**
     * Vytvorí opravenu mapu
     */
    public String[] opravenaMapu(double hustotaStien, int pocetNabojov) {
        String[] mapa;
        mapa = this.generujMapu(hustotaStien, pocetNabojov);
        mapa = this.opravMapu(mapa);
        return mapa;
    }

    /**
     * Generuje mapu s okrajmi, hráčmi náhodne rozmiestnenými stenami a nábojmi 
     */
    public String[] generujMapu(double hustotaStien, int pocetNabojov) {
        char[][] mapa = new char[RIADKY][STLPCE];

        //setnem prazne vsetko
        for (int r = 0; r < RIADKY; r++) {
            for (int s = 0; s < STLPCE; s++) {
                mapa[r][s] = '.';
            }
        }

        //setnem okraje
        for (int s = 0; s < STLPCE; s++) {
            mapa[0][s] = 'X';
            mapa[RIADKY - 1][s] = 'X';
        }
        for (int r = 0; r < RIADKY; r++) {
            mapa[r][0] = 'X';
            mapa[r][STLPCE - 1] = 'X';
        }

        //random steny
        for (int r = 1; r < RIADKY - 1; r++) {
            for (int s = 1; s < STLPCE - 1; s++) {
                if (this.random.nextDouble() < hustotaStien) {
                    mapa[r][s] = 'X';
                }
            }
        }
        
        //setnem hracov
        mapa[this.hrac1Y][this.hrac1X] = 'H';
        mapa[this.hrac2Y][this.hrac2X] = 'P';
        this.vycistiOkolieHracov(mapa, this.hrac1X, this.hrac1Y, this.hrac2X, this.hrac2Y);

        int umiestneneNaboje = 0;
        while (umiestneneNaboje < pocetNabojov) {
            int x = this.random.nextInt(STLPCE - 2) + 1;
            int y = this.random.nextInt(RIADKY - 2) + 1;
            if (mapa[y][x] == '.') {
                mapa[y][x] = 'A';
                umiestneneNaboje++;
            }
        }

        // Konverzia char[][] na String[]
        String[] vysledok = new String[RIADKY];
        for (int r = 0; r < RIADKY; r++) {
            vysledok[r] = new String(mapa[r]);
        }

        return vysledok;
    }

    /**
     * Opravuje mapu kým nespĺňa podmienku
     */
    public String[] opravMapu(String[] mapa) {
        int iteracia = 0;
        int maxIteracii = 100; 
        
        while (!this.funkcnostMapy(mapa) && iteracia < maxIteracii) {
            iteracia++;
            //Konverzia na char[][]
            char[][] mapaPole = new char[RIADKY][STLPCE];
            for (int r = 0; r < RIADKY; r++) {
                mapaPole[r] = mapa[r].toCharArray();
            }

            boolean[][] dosiahnutelne = this.najdiDosiahnutelne(mapaPole);
            this.najdiPolicko(mapaPole, dosiahnutelne);

            // Konverzia na String[]
            String[] vysledok = new String[RIADKY];
            for (int r = 0; r < RIADKY; r++) {
                vysledok[r] = new String(mapaPole[r]);
            }

            mapa = vysledok;
        }

        return mapa;
    }
    
    /**
     * metodá hlada políčka ktore nie su stena a sú obklopene stenami so 4 strán
     */
    public boolean najdiPolicko(char[][] mapaPole, boolean[][] dosiahnutelne) {
        for (int i = 1; i < RIADKY - 1; i++) {
            for (int j = 1; j < STLPCE - 1; j++) {
                if (mapaPole[i][j] != 'X' && !dosiahnutelne[i][j]) {
                    return this.vytvorPriechod(mapaPole, dosiahnutelne, i, j);
                }
            }
        }
        return false; 

    }

    /**
     * Vytvorí priechod z nedosiahnuteľného políčka k dosiahnuteľnej oblasti pomocou BFS algoritmu
     */
    public boolean vytvorPriechod(char[][] mapaPole, boolean[][] dosiahnutelne, int startR, int startS) {
        // BFS
        int maxVelkostFronty = RIADKY * STLPCE;
        int[][] fronta = new int[maxVelkostFronty][2];
        int[][] rodic = new int[RIADKY * STLPCE][2];
        int zaciatok = 0;
        int koniec = 0;

        boolean[][] navstivene = new boolean[RIADKY][STLPCE];

        fronta[koniec][0] = startR;
        fronta[koniec][1] = startS;
        koniec++;
        navstivene[startR][startS] = true;
        rodic[startR * STLPCE + startS][0] = -1;
        rodic[startR * STLPCE + startS][1] = -1;

        int[] horeDole = {-1, 1, 0, 0};
        int[] vlavoVpravo = {0, 0, -1, 1};

        int cielR = -1;
        int cielS = -1;

        while (zaciatok < koniec) {
            int r = fronta[zaciatok][0];
            int s = fronta[zaciatok][1];
            zaciatok++;
            if (dosiahnutelne[r][s] && !(r == startR && s == startS)) {
                cielR = r;
                cielS = s;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int novyR = r + horeDole[i];
                int novyS = s + vlavoVpravo[i];

                if (novyR >= 0 && novyR < RIADKY && 
                    novyS >= 0 && novyS < STLPCE && 
                    !navstivene[novyR][novyS]) {

                    navstivene[novyR][novyS] = true;
                    fronta[koniec][0] = novyR;
                    fronta[koniec][1] = novyS;
                    koniec++;

                    rodic[novyR * STLPCE + novyS][0] = r;
                    rodic[novyR * STLPCE + novyS][1] = s;
                }
            }
        }

        //rekonstrukcia cesty 
        if (cielR != -1) {
            int aktR = cielR;
            int aktS = cielS;

            while (rodic[aktR * STLPCE + aktS][0] != -1) {
                if (mapaPole[aktR][aktS] == 'X') {
                    mapaPole[aktR][aktS] = '.';
                }

                int index = aktR * STLPCE + aktS;
                aktR = rodic[index][0];
                aktS = rodic[index][1];
            }

            return true;
        }

        return false;
    }

    /**
     * Nájde všetky dosiahnuteľné políčka z pozície hráča H pomocou BFS
     */
    public boolean[][] najdiDosiahnutelne(char[][] mapaPole) {
        // BFS
        int maxVelkostFronty = RIADKY * STLPCE;
        int[][] fronta = new int[maxVelkostFronty][2];
        int zaciatok = 0;
        int koniec = 0;

        boolean[][] navstivene = new boolean[RIADKY][STLPCE];

        fronta[koniec][0] = this.hrac1Y;
        fronta[koniec][1] = this.hrac1X;
        koniec++;
        navstivene[this.hrac1Y][this.hrac1X] = true;

        int[] horeDole = {-1, 1, 0, 0};
        int[] vlavoVpravo = {0, 0, -1, 1};

        while (zaciatok < koniec) {
            int r = fronta[zaciatok][0];
            int s = fronta[zaciatok][1];
            zaciatok++;

            for (int i = 0; i < 4; i++) {
                int novyR = r + horeDole[i];
                int novyS = s + vlavoVpravo[i];

                //Pridam do fronty všetky políčka, ktoré su neni steny
                if (novyR >= 0 && novyR < RIADKY && 
                    novyS >= 0 && novyS < STLPCE && 
                    !navstivene[novyR][novyS] && 
                    mapaPole[novyR][novyS] != 'X') {

                    navstivene[novyR][novyS] = true;
                    fronta[koniec][0] = novyR;
                    fronta[koniec][1] = novyS;
                    koniec++;
                }
            }
        }

        return navstivene;
    }

    /**
     * Kontroluje funkčnosť mapy všetky políčka ktoré nie su stena musia byť dosiahnuteľné
     */
    public boolean funkcnostMapy(String[] mapa) {
        char[][] mapaPole = new char[RIADKY][STLPCE];
        for (int r = 0; r < RIADKY; r++) {
            mapaPole[r] = mapa[r].toCharArray();
        }

        int celkovePrazdne = 0;
        for (int r = 0; r < RIADKY; r++) {
            for (int s = 0; s < STLPCE; s++) {
                if (mapaPole[r][s] != 'X') {
                    celkovePrazdne++;
                }
            }
        }

        // BFS
        int maxVelkostFronty = RIADKY * STLPCE;
        int[][] fronta = new int[maxVelkostFronty][2];
        int zaciatok = 0;
        int koniec = 0;

        boolean[][] navstivene = new boolean[RIADKY][STLPCE];

        fronta[koniec][0] = this.hrac1Y;
        fronta[koniec][1] = this.hrac1X;
        koniec++;
        navstivene[this.hrac1Y][this.hrac1X] = true;
        int dostupnePolicka = 1;

        int[] horeDole = {-1, 1, 0, 0};
        int[] vlavoVpravo = {0, 0, -1, 1};

        while (zaciatok < koniec) {
            int r = fronta[zaciatok][0];
            int s = fronta[zaciatok][1];
            zaciatok++;

            for (int i = 0; i < 4; i++) {
                int novyR = r + horeDole[i];
                int novyS = s + vlavoVpravo[i];

                if (novyR >= 0 && novyR < RIADKY && 
                    novyS >= 0 && novyS < STLPCE && 
                    !navstivene[novyR][novyS] && 
                    mapaPole[novyR][novyS] != 'X') {

                    navstivene[novyR][novyS] = true;
                    fronta[koniec][0] = novyR;
                    fronta[koniec][1] = novyS;
                    koniec++;
                    dostupnePolicka++;
                }
            }
        }

        
        if (dostupnePolicka == celkovePrazdne) {
            return true;
        }
        return false;
    }

    /**
     * Odstráni 1 stenu okolo hráča okrem ookrajovej steny 
     */
    public void vycistiOkolieHracov(char[][] mapa, int hrac1X, int hrac1Y, int hrac2X, int hrac2Y) {
        int[] horeDole = {-1, 1, 0, 0};
        int[] vlavoVpravo = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int novyR = hrac1Y + horeDole[i];
            int novyS = hrac1X + vlavoVpravo[i];

            //kontrola okraja
            if (novyR > 0 && novyR < RIADKY - 1 && 
                novyS > 0 && novyS < STLPCE - 1) {
                mapa[novyR][novyS] = '.';
            }
        }

        for (int i = 0; i < 4; i++) {
            int novyR = hrac2Y + horeDole[i];
            int novyS = hrac2X + vlavoVpravo[i];

            //kontrola okraja
            if (novyR > 0 && novyR < RIADKY - 1 && 
                novyS > 0 && novyS < STLPCE - 1) {
                mapa[novyR][novyS] = '.';
            }
        }
    }
}