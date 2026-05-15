package powerUps;

import core.HernyObjekt;
import players.Hrac;
import players.Postava;

import java.awt.Image;

public class Speed extends PowerUp {

    private final int trvanie;

    public Speed(Image obrazok, int x, int y, int velkost, int trvanie) {
        super(obrazok, x, y, velkost);
        this.trvanie = trvanie;
    }

    @Override
    public void pouzi(Postava postava) {
        if (postava instanceof Hrac){
            ((Hrac) postava).activateSpeedBoost(this.trvanie);
        }
        this.oznacZobrany();
    }

}
