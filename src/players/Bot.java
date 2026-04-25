package players;

import core.HernyObjekt;
import weapons.Strela;

import java.awt.*;
import java.util.List;

public class Bot extends Hrac implements Postava {
    private HernyObjekt ciel;

    public Bot(Image obrazok, int x, int y, int velkost, boolean[] stlaceneKlavesy) {
        super(obrazok, x, y, velkost, 'L', -1, -1, -1, -1, -1, stlaceneKlavesy);
        this.x = x;
        this.y = y;
        this.sirka = velkost;
        this.vyska = velkost;
        this.obrazok = obrazok;
    }

    public void sledujCiel(Hrac ciel) {
        this.ciel = ciel;
    }

    /**
     * OVERRIDE pohybSa() — polymorfná metóda.
     * Hrac.pohybSa() číta klávesy.
     * Bot.pohybSa()  nasleduje cieľ bez klávesnice.
     */
    @Override
    public void pohybSa() {
        if (this.ciel == null) return;
        this.pohybX = 0;
        this.pohybY = 0;

        int dx = this.ciel.getX() - this.x;
        int dy = this.ciel.getY() - this.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                this.pohybX = this.rychlost;
                this.setSmerObr('R');
            }
            else {
                this.pohybX = -this.rychlost;
                this.setSmerObr('L');
            }
        } else {
            if (dy > 0) this.pohybY = this.rychlost;
            else        this.pohybY = -this.rychlost;
        }

        if (this.cooldown > 0) this.cooldown--;

        // Bot strieľa keď je blízko hráča
        if (Math.abs(dx) < 200 && Math.abs(dy) < 200) {
            this.vystrel();
        }
    }

    /**
     * OVERRIDE utoc() — Bot strieľa smerom k hráčovi, nie podľa smerObr.
     */
    @Override
    public void utoc(int cielX, int cielY, List<Strela> strely) {
        if (!this.mozeUtocit() || !this.getVystrelena()) return;
        double dx = cielX - this.x;
        double dy = cielY - this.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        if (d == 0) return;
        double rx = (dx / d) * 8;
        double ry = (dy / d) * 8;
        strely.add(new Strela(this.x + this.sirka / 2, this.y + this.sirka / 2,
                rx, ry, 1, Strela.TypStrely.NORMALNA));
        this.resetVystrelena();
        this.cooldown = 15;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}
