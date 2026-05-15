package powerUps;

import players.Postava;

import java.awt.Image;

public class Speed extends PowerUp {

    public Speed(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost);
    }

    @Override
    public void pouzi(Postava postava) {
        this.oznacZobrany();
    }

}
