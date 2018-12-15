package visualloop5;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author SUGIARTO COKROWIBOWO
 */
public class VisualLoop5 extends Application implements Runnable {

    //Loop Parameters
    private final static int MAX_FPS = 60;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    public static void main(String[] args) {
        launch(args);
    }

    private static float randomBetweenFloat(float min, float max) {
        return (float) (Math.random() * (max - min)) + min;
    }

    //Thread
    private Thread thread;
    private volatile boolean running = true;

    //Canvas
    Canvas canvas = new Canvas(700, 700);

    //KEYBOARD HANDLER
    ArrayList<String> inputKeyboard = new ArrayList<String>();

    //PLAYER
    float player_r = 20f;
    float player_speed1 = 2;
    float player_speed2 = 6;
    Player player = new Player(this.canvas, (float) canvas.getWidth() / 2.0f, (float) canvas.getWidth() - player_r, player_r, player_speed1, player_speed2, Color.DEEPSKYBLUE);
    int nyawa = 9;
    int poin = 0;
    int bomb = 7;

    //ENEMIES
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    //COLLISION
    boolean isCollided = false;

    public VisualLoop5() {
        initializeEnemy();
        resume();

    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        root.getChildren().add(canvas);
        //HANDLING KEYBOARD EVENT
        scene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                if (!inputKeyboard.contains(code)) {
                    inputKeyboard.add(code);
                    System.out.println(code);
                }
            }
        }
        );

        scene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                inputKeyboard.remove(code);
            }
        }
        );

        //HANDLING MOUSE EVENT
        scene.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {

            }
        }
        );

        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.jpg")));
        primaryStage.setTitle("Visual Loop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeEnemy() {
        int numOfEnemy = 6;
        for (int i = 0; i < numOfEnemy; i++) {
            addEnemy();
        }
    }

    private void addEnemy() {
        float jangkauanEnemy = (float) ((3.0f / 4.0f) * canvas.getHeight());
        float enemy_r = 20f;
        float enemy_speed1 = 1;
        float enemy_speed2 = 6;
        float enemy_speed = randomBetweenFloat(enemy_speed1, enemy_speed2);
        float enemy_cx = randomBetweenFloat(0, (float) canvas.getWidth());
        float enemy_cy = randomBetweenFloat(0, jangkauanEnemy);
        Enemy enemy = new Enemy(this.canvas, enemy_cx, enemy_cy, enemy_r, enemy_speed, Color.CRIMSON);
        enemies.add(enemy);
    }

    //THREAD
    private void resume() {
        reset();
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    //THREAD
    private void pause() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //LOOP
    private void reset() {

    }

    //LOOP
    private void update() {
        if (player != null) {
            if (inputKeyboard.contains("LEFT")) {
                player.moveLeft();
            } else if (inputKeyboard.contains("RIGHT")) {
                player.moveRight();
            }
            if (inputKeyboard.contains("SPACE")) {
                player.speedUp();
            } else {
                player.speedDown();
            }

            if (inputKeyboard.contains("X")) {
                player.menembak();
            }
        }

        if (enemies != null && !enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                enemy.move();
            }
        }

        isCollided = false;
        //Cek Tembakan Player
        int numEnemyTertembak = 0;
        if (player != null && player.misils != null && !player.misils.isEmpty() && enemies != null && !enemies.isEmpty()) {
            for (int i = player.misils.size() - 1; i >= 0; i--) {
                for (int j = enemies.size() - 1; j >= 0; j--) {
                    if (coliisionDetection(player.misils.get(i), enemies.get(j))) {
                        isCollided = true;
                        enemies.remove(j);                        
                        player.misils.remove(i);
                        poin++;
                        numEnemyTertembak++;
                        break;
                    }
                }
            }
        }

        //cek tembakan Enemy
        if (player != null && enemies != null && !enemies.isEmpty()) {
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                if (enemy.misils != null && !enemy.misils.isEmpty() && player != null) {
                    for (int j = enemy.misils.size() - 1; j >= 0; j--) {
                        if (coliisionDetection(enemy.misils.get(j), player)) {
                            isCollided = true;
                            enemy.misils.remove(j);
                            nyawa--;
                        }
                        if (nyawa <= 0) {
                            player = null;
                            break;
                        }
                    }
                }
                if (player == null) {
                    break;
                }
            }
        }
        
        //menambahkan Enemy baru sebanyak Enemy yang tertembak
        for(int i=0;i<numEnemyTertembak;i++){
            addEnemy();
        }

    }

    private boolean coliisionDetection(Lingkaran lingkaran_1, Lingkaran lingkaran_2) {
        boolean result = false;
        float cx1 = lingkaran_1.cx;
        float cy1 = lingkaran_1.cy;
        float r1 = lingkaran_1.radius;
        float cx2 = lingkaran_2.cx;
        float cy2 = lingkaran_2.cy;
        float r2 = lingkaran_2.radius;
        Circle c1 = new Circle(cx1, cy1, r1);
        Circle c2 = new Circle(cx2, cy2, r2);
        Shape shape = Path.intersect(c1, c2);
        if (shape.getBoundsInLocal().getWidth() != -1 || shape.getBoundsInLocal().getHeight() != -1) {
            result = true;
        }
        return result;
    }

    //LOOP
    private void draw() {
        try {
            if (canvas != null) {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Color bgColor = Color.WHITESMOKE;
                if (isCollided) {
                    bgColor = Color.valueOf("#f1c40f");
                }
                gc.setFill(bgColor);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                //Draw Player and Enemy
                if (player != null) {
                    player.draw();
                }
                if (enemies != null && !enemies.isEmpty()) {
                    for (Enemy enemy : enemies) {
                        enemy.draw();
                    }
                }

                //Draw Misil
                if (player != null) {
                    player.drawMisils();
                }
                if (enemies != null && !enemies.isEmpty()) {
                    for (Enemy enemy : enemies) {
                        enemy.drawMisils();
                    }
                }

                //Draw info
                gc.setFill(Color.BLACK);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText("POIN  : " + poin, 10, 20);
                gc.fillText("NYAWA : " + nyawa, 10, 40);
                gc.fillText("BOM   : " + bomb, 10, 60);
                if(nyawa<=0){
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText("GAME OVER", canvas.getWidth()*0.5f, canvas.getHeight()*0.5f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long beginTime;
        long timeDiff;
        int sleepTime = 0;
        int framesSkipped;
        //LOOP WHILE running = true;
        while (running) {
            try {
                synchronized (this) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;
                    update();
                    draw();
                }
                timeDiff = System.currentTimeMillis() - beginTime;
                sleepTime = (int) (FRAME_PERIOD - timeDiff);
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }
                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                    update();
                    sleepTime += FRAME_PERIOD;
                    framesSkipped++;
                }
            } finally {

            }
        }
    }
}