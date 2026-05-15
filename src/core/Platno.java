package core;



import javax.swing.JFrame;
import java.awt.Dimension;


/**
 * Vytvorenie plátna na ktorom sa bude hra hrať.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Platno {
    /**
     * max počet riadkov do ktorých sa môžu vykreslovať štvorce
     */
    private static  final int RIADKY = 23; 
    
    /**
     * max počet stlpcov do ktorých sa môžu vykreslovať štvorce
     */
    private static  final int STLPCE = 26;
    
    /**
     * velkosť strany štvorca
     */
    private static  final int VELKOST_S = 32;
    
    private int sirkaPlatna = this.STLPCE * this.VELKOST_S - 19;
    private int vyskaPlatna = this.RIADKY * this.VELKOST_S;
    
    private Obtiaznost obtaznost;
    private TypHry typ;
    private TypEnemaka typEnemaka;

    
    public Platno(Obtiaznost obtaznost, TypHry typ, TypEnemaka typEnemaka) {
        this.obtaznost = obtaznost;
        this.typ = typ;

        JFrame platno = new JFrame();  
        platno.setSize(this.sirkaPlatna, this.vyskaPlatna); 
        platno.setResizable(false); 
        platno.setLocationRelativeTo(null); 
        platno.setPreferredSize(new Dimension(this.sirkaPlatna, this.vyskaPlatna)); 
        platno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (typ == TypHry.MULTIPLAYER) {
            platno.setTitle("Multiplayer");
            Multiplayer hra = new Multiplayer(obtaznost);
            platno.add(hra);
            hra.requestFocus();

        } else {
            if (typEnemaka == TypEnemaka.BOT) {
                platno.setTitle("Bot");
                Singleplayer hra = new Singleplayer(obtaznost, TypEnemaka.BOT);
                platno.add(hra);
                hra.requestFocus();

            } else {
                platno.setTitle("Boss");
                Singleplayer hra = new Singleplayer(obtaznost, TypEnemaka.BOSS);
                platno.add(hra);
                hra.requestFocus();
            }
        }

        platno.pack();
        platno.setVisible(true);
        
    }
}
