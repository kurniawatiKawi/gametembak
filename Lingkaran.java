package visualloop5;
 
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Lingkaran {
    Canvas canvas   = null;
    float cx,cy,speed, radius;
    Color color = Color.WHITESMOKE;
    
    public void moveLeft(){
        cx-=speed;
        if(cx<=0){
            cx=0;
        }
    }
    
    public void moveRight(){
        cx+=speed;
        if(canvas!=null&&cx>=canvas.getWidth()){
            cx = (float)canvas.getWidth();
        }
    }
    
    public void moveUp(){
        cy-=speed;
        if(cy<=0){
            cy=0;
        }
    }
    
    public void moveDown(){
        cy+=speed;
        if(canvas!=null&&cy>=canvas.getHeight()){
            cy = (float)canvas.getWidth();
        }
    }
    
    public void draw(){
        try{
            if(canvas!=null){
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.save();
                gc.translate(cx, cy);
                gc.setFill(color);
                gc.fillOval(-radius, -radius, 2.0*radius, 2.0*radius);
                gc.restore();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
