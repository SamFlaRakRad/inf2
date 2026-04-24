package core;

import players.Hrac;
import weapons.Strela;

import java.util.ArrayList;
import java.awt.Image;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
/**
 * Hlavná trieda Hra v ktorej je napísana celá mechanika hry.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Hra extends JPanel implements ActionListener, KeyListener {
    /**
     * počet riadkov 
     */
    private static  final int RIADKY = 20;
    
    /**
     * počet stlpcov 
     */
    private static  final int STLPCE = 25;
    
    /**
     * velkosť strany štvorca
     */
    private static  final int VELKOST_S = 32;
    
    private ArrayList<Stvorec> stvorce;
    private ArrayList<Stvorec> naboje;
    private ArrayList<Strela>strelyH1;
    private ArrayList<Strela>strelyH2;
    
    private Image stena;
    private Image naboj;
    private Image hrac1Vpravo;
    private Image hrac1Vlavo;
    private Image hrac2Vpravo;
    private Image hrac2Vlavo;

    private Stvorec steny;
    private Stvorec obrHraca1;
    private Stvorec obrHraca2;
    private Stvorec ammo;

    private Hrac hrac1;
    private Hrac hrac2;
    private boolean[] stlaceneKlavesy = new boolean[256];

    private int zasobnikH1;
    private int zasobnikH2;
    
    private int zacinajuceNaboje;
    private int zasobniky;
    
    private Timer gameLoop;
    private Timer casovac;
    
    private boolean remiza = false;
    private boolean vyhralH1 = false;
    private boolean vyhralH2 = false;
    private boolean stopnutaHra = false;
    private String[] mapa;
    private Obtiaznost obtiaznost;
    
    /**
     * Konštruktor triedy Hra
     */
    public Hra(Obtiaznost obtiaznost) {
        this.obtiaznost = obtiaznost;
        if (this.obtiaznost.equals(obtiaznost.LAHKA)) {
            this.zacinajuceNaboje = 20;
            this.zasobniky = this.zacinajuceNaboje;
            this.zasobnikH1 = this.zacinajuceNaboje;
            this.zasobnikH2 = this.zacinajuceNaboje;
        } else if (this.obtiaznost.equals(obtiaznost.STREDNA)) {
            this.zacinajuceNaboje = 10;
            this.zasobniky = this.zacinajuceNaboje;
            this.zasobnikH1 = this.zacinajuceNaboje;
            this.zasobnikH2 = this.zacinajuceNaboje;
        } else if (this.obtiaznost.equals(obtiaznost.TAZKA)) {
            this.zacinajuceNaboje = 5;
            this.zasobniky = this.zacinajuceNaboje;
            this.zasobnikH1 = this.zacinajuceNaboje;
            this.zasobnikH2 = this.zacinajuceNaboje;
        }
        this.stvorce = new ArrayList<>();
        this.naboje = new ArrayList<>();

        this.strelyH1 = new ArrayList<Strela>();
        this.strelyH2 = new ArrayList<Strela>();

        this.hrac1 = new Hrac('D', KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.hrac2 = new Hrac('L', KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        this.addKeyListener(this);
        this.setFocusable(true);
        
        Generator generator = new Generator();
        this.mapa = generator.vytvorMapu(obtiaznost);
        
        /*
         * priradenie obrázkov ako ikony za stvorce
         * getClass().getResource je implementovaný class loader pridaný aby sa obrazky načítavali spravne mimo blueJ
         */
        this.stena = new ImageIcon(getClass().getResource("/images/stena.png")).getImage();
        this.naboj = new ImageIcon(getClass().getResource("/images/naboje.png")).getImage();
        this.hrac1Vpravo = new ImageIcon(getClass().getResource("/images/hrac1Vpravo.png")).getImage();
        this.hrac1Vlavo = new ImageIcon(getClass().getResource("/images/hrac1Vlavo.png")).getImage();
        this.hrac2Vpravo = new ImageIcon(getClass().getResource("/images/hrac2Vpravo.png")).getImage();
        this.hrac2Vlavo = new ImageIcon(getClass().getResource("/images/hrac2Vlavo.png")).getImage();

        
        this.nacitajMapu();

        //časovač koľko krát sa má zopakovať metoda actionPerformed
        //každých 35 stotin sekundy vykresluje obraz na platno 
        this.gameLoop = new Timer(35, this);
        this.gameLoop.start();

    }
    
    /**
     * metoda remízi ked dojdú hráčom náboje
     */
    public void remiza() {
        if (this.zasobnikH1 == 0 && this.zasobnikH2 == 0 && this.naboje.size() == 0) {
            this.remiza = true;
        }
    }
  
    
    /**
     * načítavanie mapy z pola
     */
    public void nacitajMapu() {
        /* r = riadok
         * s = stlpec
         */
        for (int r = 0; r < this.RIADKY; r++) {
            for (int s = 0; s < this.STLPCE; s++) {
                String riadok = this.mapa[r];  
                char charakterNaMape = riadok.charAt(s); 

                int x = s * this.VELKOST_S;
                int y = r * this.VELKOST_S;

                if (charakterNaMape == 'H') {
                    this.obrHraca1 = new Stvorec(this.hrac1Vpravo, x, y, this.VELKOST_S, this.VELKOST_S);
                }

                if (charakterNaMape == 'P') {
                    this.obrHraca2 = new Stvorec(this.hrac2Vlavo, x, y, this.VELKOST_S, this.VELKOST_S);
                }

                if (charakterNaMape == 'X') {
                    Stvorec novaStena = new Stvorec(this.stena, x, y, this.VELKOST_S, this.VELKOST_S);
                    this.stvorce.add(novaStena);
                }

                if (charakterNaMape == 'A') {
                    Stvorec noveNaboje = new Stvorec(this.naboj, x, y, this.VELKOST_S, this.VELKOST_S);
                    this.naboje.add(noveNaboje);
                }
            }
        }
    }

    /**
     * vykreslovanie hry na plátno prekýva metódu paintComponent s JPanelu 
     */
    @Override
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);
        this.kresli(g);
        this.strelba(g);
        
    }
    
    /**
     * vykreslovanie jednotlivých častí na mape
     */
    public void kresli(Graphics g) {
        //vykreslenie hráča1
        g.drawImage(this.obrHraca1.getObrazok(), this.obrHraca1.getX(), this.obrHraca1.getY(), this.obrHraca1.getStranaA(), this.obrHraca1.getStranaA(), null);
        
        //vykreslenie hráča2
        g.drawImage(this.obrHraca2.getObrazok(), this.obrHraca2.getX(), this.obrHraca2.getY(), this.obrHraca2.getStranaA(), this.obrHraca2.getStranaA(), null);
        
        //vykreslenie stien
        for (Stvorec nakreslenaStena : this.stvorce) {
            g.drawImage(nakreslenaStena.getObrazok(), nakreslenaStena.getX(), nakreslenaStena.getY(), nakreslenaStena.getStranaA(), nakreslenaStena.getStranaA(), null);
        }

        //vykreslenie nabojov
        for (Stvorec nakresleneNaboje : this.naboje) {
            g.drawImage(nakresleneNaboje.getObrazok(), nakresleneNaboje.getX(), nakresleneNaboje.getY(), nakresleneNaboje.getStranaA(), nakresleneNaboje.getStranaA(), null);
        }

        //naboje counter a text pre 3 konce hry
        g.setFont(new Font("Arial", Font.BOLD, 25));
        if (this.vyhralH1) {
            g.drawString("Hrac1 vyhral", 310, 670);
            this.stopnutaHra = true;
        } else if (this.vyhralH2) {
            g.drawString("Hrac2 vyhral", 310, 670);
            this.stopnutaHra = true;
        } else if (this.remiza) {
            g.drawString("Remíza došli vám náboje", 280, 670);
            this.stopnutaHra = true;
        } else {
            g.drawString("Ammo: " + this.zasobnikH1, 50, 670);
            g.drawString("Ammo: " + this.zasobnikH2, 650, 670);
        }
        
        //text pre resetovanie hry
        if (this.stopnutaHra) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("Stlač ESC pre znovuspustenie", 50, 400);
        }
    }
    
    /**
     * vykreslovanie pohybov na mape
     */
    public void pohybNaMape() {
        int novaX1 = this.obrHraca1.getX() + this.hrac1.getPohybX();
        int novaY1 = this.obrHraca1.getY() + this.hrac1.getPohybY();
    
        int novaX2 = this.obrHraca2.getX() + this.hrac2.getPohybX();
        int novaY2 = this.obrHraca2.getY() + this.hrac2.getPohybY();
    
        this.obrHraca1.setX(novaX1);
        this.obrHraca1.setY(novaY1);
        
        this.obrHraca2.setX(novaX2);
        this.obrHraca2.setY(novaY2);
        
        //kontrola aby hrac nepresiel cez stenu 
        for (Stvorec wall : this.stvorce) {
            if (this.dotyk(this.obrHraca1, wall)) {
                this.obrHraca1.setX(novaX1 - this.hrac1.getPohybX());
                this.obrHraca1.setY(novaY1 - this.hrac1.getPohybY());
            }
        }
        
        
        for (Stvorec wall : this.stvorce) {
            if (this.dotyk(this.obrHraca2, wall)) {
                this.obrHraca2.setX(novaX2 - this.hrac2.getPohybX());
                this.obrHraca2.setY(novaY2 - this.hrac2.getPohybY());
            }
        }
           
        //Mením smery obrázkov podľa smeru kam práve ide
        if (this.hrac1.getSmerObr() == 'L') {
            this.obrHraca1.setImage(this.hrac1Vlavo);
        } else {
            this.obrHraca1.setImage(this.hrac1Vpravo);
        }
        
        if (this.hrac2.getSmerObr() == 'L') {
            this.obrHraca2.setImage(this.hrac2Vlavo);
        } else {
            this.obrHraca2.setImage(this.hrac2Vpravo);
        }
    }

    /**
     * nastavenie strely vedla hrača podla toho ako je otočeny
     */
    public void smerStrely() {
        if (this.hrac1.getVystrelena() && this.zasobnikH1 > 0) {
            this.zasobnikH1--;
            Strela novaStrela = new Strela();
            if (this.hrac1.getSmerObr() == 'L') {
                novaStrela.setX(this.obrHraca1.getX() - this.VELKOST_S / 2);
                novaStrela.setRychlostX(-10);
            } else { // 'R'
                novaStrela.setX(this.obrHraca1.getX() + this.VELKOST_S);
                novaStrela.setRychlostX(10);
            }
            novaStrela.setY(this.obrHraca1.getY() + this.VELKOST_S / 4);
            this.strelyH1.add(novaStrela);
            this.hrac1.resetVystrelena();
        }
    
        if (this.hrac2.getVystrelena() && this.zasobnikH2 > 0) {
            this.zasobnikH2--;
            Strela novaStrela = new Strela();
            if (this.hrac2.getSmerObr() == 'L') {
                novaStrela.setX(this.obrHraca2.getX() - this.VELKOST_S / 2);
                novaStrela.setRychlostX(-10);
            } else { // 'R'
                novaStrela.setX(this.obrHraca2.getX() + this.VELKOST_S);
                novaStrela.setRychlostX(10);
            }
            novaStrela.setY(this.obrHraca2.getY() + this.VELKOST_S / 4);
            this.strelyH2.add(novaStrela);
            this.hrac2.resetVystrelena();
        }
    }
    
    /** 
     * vykreslovanie a pohyb striel 
     */
    public void strelba(Graphics g) {
        for (int i = 0; i < this.strelyH1.size(); i++) {
            Strela strela = this.strelyH1.get(i);
            strela.paint(g);
            strela.pohyb();
            
            //Ak sa strela dotkne steny tak životnost strely skončí
            for (Stvorec wall : this.stvorce) {
                if (this.dotykStrely(strela, wall)) {
                    this.strelyH1.remove(i);
                    break;
                }
            }
            
            //Ak sa strela dotkne druheho hráča tak sa skončí hra
            if (this.dotykStrely(strela, this.obrHraca2)) {
                this.vyhralH1 = true;
            }
        }

        
        for (int i = 0; i < this.strelyH2.size(); i++) {
            Strela strela = this.strelyH2.get(i);
            strela.paint(g);
            strela.pohyb();
            
            //Ak sa strela dotkne steny tak životnost strely skončí
            for (Stvorec wall : this.stvorce) {
                if (this.dotykStrely(strela, wall)) {
                    this.strelyH2.remove(i);
                    break;
                }
            }
            
            //Ak sa strela dotkne prvého hráča tak sa skončí hra
            if (this.dotykStrely(strela, this.obrHraca1)) {
                this.vyhralH2 = true;
            }
        }

    }
    
    /**
     * pridávanie nábojov hráčovy ktorý zoberie náboje na mape
     */
    public void prebitie() {
        Stvorec zobrateNaboje = null;
        for (Stvorec nabojeNaMape : this.naboje) {
            if (this.dotyk(this.obrHraca1, nabojeNaMape)) {
                zobrateNaboje = nabojeNaMape;
                this.zasobnikH1 += this.zacinajuceNaboje;
                this.hrac1.resetVystrelena();
            }
            
            if (this.dotyk(this.obrHraca2, nabojeNaMape)) {
                zobrateNaboje = nabojeNaMape;
                this.zasobnikH2 += this.zacinajuceNaboje;
                this.hrac2.resetVystrelena();
            }
        }

        this.naboje.remove(zobrateNaboje);

    }
    
    /**
     * program na rozoznanie dotykov mezdi stvorcami
     * Axis-Aligned Bounding Box collision detection algorithm 
     * zdroje:
     * https://stackoverflow.com/questions/41370911/check-if-two-rectangles-overlap
     * ChatGPT
     */
    public boolean dotyk(Stvorec a, Stvorec b) {
        return  a.getX() < b.getX() + b.getStranaA() &&
        a.getX() + a.getStranaA() > b.getX() &&
        a.getY() < b.getY() + b.getStranaA() &&
        a.getY() + a.getStranaA() > b.getY();
    }

    /**
     * program na rozoznanie dotykov mezdi strelou a stvorcami
     * upravená metóda dotyk
     */
    public boolean dotykStrely(Strela strela, Stvorec stvorec) {
        return  strela.getX() < stvorec.getX() + stvorec.getStranaA() &&
        strela.getX() + strela.getStranaStrely() > stvorec.getX() &&
        strela.getY() < stvorec.getY() + stvorec.getStranaA() &&
        strela.getY() + strela.getStranaStrely() > stvorec.getY();

    }

    /**
     * táto metóda je súčasťou actionListeneru 
     * vykonáva sa vždy keď sa stlačí niektorá klávesa pohybu alebo streľby  
     */
    public void actionPerformed(ActionEvent e) {
        this.hrac1.spracujPohyb(this.stlaceneKlavesy);
        this.hrac2.spracujPohyb(this.stlaceneKlavesy);
        this.pohybNaMape();
        this.smerStrely();
        this.prebitie();
        this.remiza();
        this.repaint(); 
        if (this.stopnutaHra) {
            this.gameLoop.stop();
        }
    } 

    /**
     * Reštartovanie hry
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int kod = e.getKeyCode();
        if (kod < 256) {
            this.stlaceneKlavesy[kod] = true;
        }
    
        // Detekcia streľby (jednorazovo)
        if (kod == KeyEvent.VK_SPACE) {
            this.hrac1.vystrel();
        } else if (kod == KeyEvent.VK_ENTER) {
            this.hrac2.vystrel();
        }
    
        // Reštart hry cez ESC
        if (this.stopnutaHra && kod == KeyEvent.VK_ESCAPE) {
            this.nacitajMapu();
            this.remiza = false;
            this.vyhralH1 = false;
            this.vyhralH2 = false;
            this.stopnutaHra = false;
            this.hrac1.setSmerObr('D');
            this.hrac2.setSmerObr('L');
            this.strelyH1.clear();
            this.strelyH2.clear();
            this.zasobnikH1 = this.zacinajuceNaboje;
            this.zasobnikH2 = this.zacinajuceNaboje;
            this.gameLoop.start();
        }
    }
       
    @Override
    public void keyReleased(KeyEvent e) {
        int kod = e.getKeyCode();
        if (kod < 256) {
            this.stlaceneKlavesy[kod] = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    
    }
}

