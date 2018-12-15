package visualloop5;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Enemy extends Lingkaran{
    
    ArrayList<Misil> misils = new ArrayList<Misil>();
    int t = 0, T = 40;//waktu loading senjata musuh
    Arah arah = Arah.DEFAULT;
    int a=0,A=60;

    public Enemy(Canvas canvas, float cx, float cy, float radius, float speed, Color color) {
        this.canvas = canvas;
        this.cx = cx;
        this.cy = cy;
        this.speed = speed;
        this.radius = radius;
        this.color = color;
    }
    
    public void menembak() {
        t++;
        if (t >= T) {
            t = 0;
            T = randomBetweenInt(40, 100);
            float radiusMisil = 4;
            float speedMisil = 10;
            Misil misil = new Misil(this.canvas, cx, cy, radiusMisil, speedMisil, Arah.DOWN, Color.BLACK);
            misils.add(misil);
        }

    }
    
    public void move(){
        //Enemy bergerak secara acak horizontal
        a++;
        if(a>=A){
            a = 0;
            A = randomBetweenInt(50, 80);
            int random = randomBetweenInt(0, 1000);
            if(random%2==0){
                this.arah = Arah.LEFT;
            }else{
                this.arah = Arah.RIGHT;
            }
        }
        
        if(this.arah.equals(Arah.RIGHT)){
            this.moveRight();
        }else if(this.arah.equals(Arah.LEFT)){
            this.moveLeft();
        }
        
        //Enemy bergerak sambil menembak
        int random = randomBetweenInt(0, 1000);
        if(random%2==0){
            menembak();
        }
        
    }
    private static int randomBetweenInt(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
    
    public void drawMisils() {
        if (misils != null && !misils.isEmpty()) {
            for (Misil misil : misils) {
                misil.draw();
                misil.update();
                if (misil.cy >= canvas.getHeight()) {
                    misils.remove(misil);
                }
            }
        }
    }
    
}
