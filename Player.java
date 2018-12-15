package visualloop5;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Player extends Lingkaran {

    float speed_1, speed_2;
    ArrayList<Misil> misils = new ArrayList<Misil>();
    int t = 0, T = 30;//waktu loading senjata

    public Player(Canvas canvas, float cx, float cy, float radius, float speed_1, float speed_2, Color color) {
        this.canvas = canvas;
        this.cx = cx;
        this.cy = cy;
        this.speed_1 = speed_1;
        this.speed_2 = speed_2;
        this.speed = speed_1;
        this.radius = radius;
        this.color = color;
    }

    public void speedUp() {
        this.speed = this.speed_2;
    }

    public void speedDown() {
        this.speed = speed_1;
    }

    public void menembak() {
        t++;
        if (t >= T) {
            t = 0;
            float radiusMisil = 5;
            float speedMisil = 10;
            Misil misil = new Misil(this.canvas, cx, cy, radiusMisil, speedMisil, Arah.UP, Color.ORANGE);
            misils.add(misil);
        }

    }

    public void drawMisils() {
        if (misils != null && !misils.isEmpty()) {
            for (Misil misil : misils) {
                misil.draw();
                misil.update();
                if (misil.cy <=0) {
                    misils.remove(misil);
                }
            }
        }
    }

}
