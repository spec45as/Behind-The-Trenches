package gameframework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Framework extends Canvas {

    private static final long serialVersionUID = 7756423071627919896L;
    private static int frameWidth;
    private static int frameHeight;
    private static float ALPHA_X;
    private static float ALPHA_Y;
    public static final long secInNanosec = 1000000000L;
    public static final long milisecInNanosec = 1000000L;
    private final static int GAME_FPS = 30;
    public static int FPS_COUNTER = 0;
    public static int FPS_COUNT = 0;
    public static long FPS_SEC_COUNTER = 0;
    private final static long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    public static enum GameState {
        STARTING, VISUALIZING, WIN, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER_STRATEGY, GAMEOVER, MAIN_MENU_STRATEGY, RETURN_TO_MENU
    }

    public static GameState gameState;
    private static Gamemode game;
    private static long gameTime;
    private long lastTime;

    public static long getUpdateTime() {
        return GAME_UPDATE_PERIOD;
    }

    public static Gamemode activeGame() {
        return game;
    }

    public static long CurGameTime() {
        return gameTime / 1000000l;
    }

    private Font font;

    @SuppressWarnings("unused")
    private BufferedImage menubackground;

    public Framework() {
        super();

        gameState = GameState.VISUALIZING;

        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }
        };
        gameThread.start();

    }

    private void Initialize() {
        font = new Font("monospaced", Font.BOLD, 28);
    }

    private void LoadContent() {
        try {
            URL menuBorderImgUrl = this.getClass().getResource("/images/menubackground.png");
            menubackground = ImageIO.read(menuBorderImgUrl);

        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static int genRand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private void GameLoop() {

        // нужны для ожидания развертывания фрейма до нормальных размеров
        long visualizingTime = 0;
        long lastVisualizingTime = System.nanoTime();

        // для сна по GAME_FPS.
        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();

            switch (gameState) {
            case PLAYING:
                gameTime = gameTime + (System.nanoTime() - lastTime);
                lastTime = System.nanoTime();
                game.UpdateGame(gameTime, mousePosition());
                // lastTime = System.nanoTime();
                // тут и должно быть
                break;
            case GAMEOVER:
                // ...
                break;
            case WIN:
                // ...
                break;
            case MAIN_MENU:
                // ...
                break;
            case OPTIONS:
                // ...
                break;
            case GAME_CONTENT_LOADING:
                // ...
                break;
            case STARTING:
                Initialize();
                LoadContent();
                gameState = GameState.MAIN_MENU;
                newGame();
                break;
            case VISUALIZING:
                if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                    setFrameWidth(this.getWidth());
                    setFrameHeight(this.getHeight());
                    setALPHA_X(((float) getFrameWidth() / (float) Window.getWindowSizeX()));
                    setALPHA_Y(((float) getFrameHeight() / (float) Window.getWindowSizeY()));
                    gameState = GameState.STARTING;
                } else {
                    visualizingTime += System.nanoTime() - lastVisualizingTime;
                    lastVisualizingTime = System.nanoTime();
                }
                break;
            default:
                break;
            }

            repaint();

            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec;

            FPS_COUNTER++;
            if ((System.nanoTime() - FPS_SEC_COUNTER) >= secInNanosec) {
                FPS_COUNT = FPS_COUNTER;
                FPS_COUNTER = 0;
                FPS_SEC_COUNTER = System.nanoTime();
            }

            if (timeLeft < 10)
                timeLeft = 10;
            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void Draw(Graphics2D g2d) {
        switch (gameState) {
        case PLAYING:
            game.Draw(g2d, mousePosition(), gameTime);
            break;
        case GAMEOVER_STRATEGY:
            g2d.setColor(Color.WHITE);
            g2d.drawString("Ваши территории были захвачены, либо вы предпочли сдаться врагу.", getFrameWidth() / 2 - 200,
                    getFrameHeight() / 4 + 30);
            g2d.drawString("Нажмите ESC для выхода в главное меню.", getFrameWidth() / 2 - 200, getFrameHeight() / 4 + 60);
            g2d.setFont(font);
            g2d.drawString("Поражение!", getFrameWidth() / 2 - 190, getFrameHeight() / 4);
            break;
        case GAMEOVER:
            g2d.setColor(Color.WHITE);
            g2d.drawString("Вы не смогли захватить и удерживать вражеские позиции, либо потеряли свои.", getFrameWidth() / 2 - 200,
                    getFrameHeight() / 4 + 30);
            g2d.drawString("Нажмите ESC для выхода в главное меню.", getFrameWidth() / 2 - 200, getFrameHeight() / 4 + 60);
            g2d.setFont(font);
            g2d.drawString("Поражение!", getFrameWidth() / 2 - 190, getFrameHeight() / 4);
            break;
        case WIN:
            g2d.setColor(Color.WHITE);
            g2d.drawString("Вы успешно справились с задачей и разгромили врага!", getFrameWidth() / 2 - 200, getFrameHeight() / 4 + 30);
            g2d.drawString("Нажмите ESC для выхода в главное меню.", getFrameWidth() / 2 - 200, getFrameHeight() / 4 + 60);
            g2d.setFont(font);
            g2d.drawString("Победа!", getFrameWidth() / 2 - 190, getFrameHeight() / 4);
            break;
        case MAIN_MENU_STRATEGY:
            g2d.setColor(Color.orange);
            font = new Font("monospaced", Font.BOLD, 14);
            g2d.setFont(font);

            g2d.drawString("Данный режим находится в режиме разработке и предназначен только для технического тестирования!",
                    getFrameWidth() / 2 - 370, getFrameHeight() / 2 - 60);
            font = new Font("monospaced", 0, 14);
            g2d.setFont(font);
            g2d.setColor(Color.white);
            g2d.drawString("Нажмите любую кнопку чтобы начать, ESC для выхода.", getFrameWidth() / 2 - 185, getFrameHeight() / 2 - 40);

            break;
        case MAIN_MENU:
            g2d.setColor(Color.orange);

            g2d.drawString("Нажмите любую кнопку чтобы начать, ESC для выхода.", getFrameWidth() / 2 - 170, getFrameHeight() / 2 - 40);
            g2d.setColor(Color.white);

            g2d.drawString("Задача - за 10 минут набрать больше очков, чем противник.", getFrameWidth() / 2 - 190,
                    getFrameHeight() / 2 - 40 + 32);
            g2d.drawString("Очки начисляются стороне, если она имеет больше флагов чем противник.", getFrameWidth() / 2 - 240,
                    getFrameHeight() / 2 - 40 + 64);

            g2d.setColor(Color.white);

            break;
        case OPTIONS:
            // ...
            break;
        case GAME_CONTENT_LOADING:
            g2d.setColor(Color.white);
            g2d.drawString("Загрузка...", getFrameWidth() / 2 - 50, getFrameHeight() / 2);
            break;
        default:
            break;
        }
        font = new Font("monospaced", Font.BOLD, 14);
        g2d.setFont(font);

        g2d.setColor(Color.white);
        drawMenuBackground(g2d);
    }

    private void newGame() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new DemoBattle(this);
    }

    public void newCompany() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new StrategicMode(this);
    }

    public void newBattle() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new BattleMode(this);
    }

    public void exitToMenu() {
        gameTime = 0;
        lastTime = System.nanoTime();

        game = new DemoBattle(this);
        gameState = GameState.PLAYING;
    }

    @SuppressWarnings("unused")
    private void restartGame() {

        gameTime = 0;
        lastTime = System.nanoTime();

        game.RestartGame();
        gameState = GameState.PLAYING;
    }

    public Point mousePosition() {
        try {
            Point mp = this.getMousePosition();

            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    @Override
    public void keyReleasedFramework(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (gameState == GameState.MAIN_MENU || gameState == GameState.MAIN_MENU_STRATEGY) {
                gameState = GameState.VISUALIZING;
                return;
            } else if (gameState == GameState.GAMEOVER || gameState == GameState.GAMEOVER_STRATEGY) {
                newGame();
                return;
            } else if (gameState == GameState.WIN) {
                newGame();
                return;
            } else if (game != null && game.getClass().getSimpleName().equals("DemoBattle")) {
                System.exit(0);
            } else if (game != null && game.getClass().getSimpleName().equals("BattleMode")) {
                gameState = GameState.GAMEOVER;
                return;
            } else if (game != null && game.getClass().getSimpleName().equals("StrategicMode")) {
                game.getSoundPlayer().stopMenuSound();
                gameState = GameState.GAMEOVER_STRATEGY;
                return;
            } else {
                System.exit(0);
            }
        }
        switch (gameState) {
        case GAMEOVER:
        case GAMEOVER_STRATEGY:
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                newGame();
            break;
        case WIN:
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                newGame();
            break;
        case MAIN_MENU:
            newBattle();
            break;
        case MAIN_MENU_STRATEGY:
            newCompany();
            break;
        default:
            break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private void drawMenuBackground(Graphics2D g2d) {
        // g2d.drawImage(menubackground, 0, 0, getFrameWidth(),
        // getFrameHeight(), null);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Pre-Alhpa Ver.", 7, getFrameHeight() - 5);

    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        Framework.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        Framework.frameHeight = frameHeight;
    }

    public static float getALPHA_X() {
        return ALPHA_X;
    }

    public void setALPHA_X(float aLPHA_X) {
        ALPHA_X = aLPHA_X;
    }

    public static float getALPHA_Y() {
        return ALPHA_Y;
    }

    public void setALPHA_Y(float aLPHA_Y) {
        ALPHA_Y = aLPHA_Y;
    }

}
