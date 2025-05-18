package gameframework.libs.other;

import gameframework.libs.JarDirLoader;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public class Resources {

    public ArrayList<BufferedImage> uk_sold_up_deaths = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> uk_sold_down_deaths = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> ger_sold_up_deaths = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> ger_sold_down_deaths = new ArrayList<BufferedImage>();

    public Map<String, BufferedImage> allTextures = new HashMap<String, BufferedImage>();
    public Map<String, BufferedReader> allTxts = new HashMap<String, BufferedReader>();

    public ArrayList<BufferedImage> playerBloodImages = new ArrayList<BufferedImage>();
    public BufferedImage rifleBullet;
    public BufferedImage rifleBulletFlash;

    public Cursor aimCursor;
    public Cursor normalCursor;
    public Cursor crosshairCursor;
    public Cursor noCursor;

    public BufferedImage mortarShell;
    public BufferedImage mortarShellFlash;
    public BufferedImage mortarShellExplosion;
    public BufferedImage mortarExplosionSprite;

    public ArrayList<BufferedImage> mapTextures = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> mapTexturesGrassCover = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> mapTrenchesCover = new ArrayList<BufferedImage>();

    public BufferedImage mapDirt;

    public ArrayList<BufferedImage> artExplosions = new ArrayList<BufferedImage>();
    public ArrayList<BufferedImage> brokenTrees = new ArrayList<BufferedImage>();

    public ArrayList<URL> mainMenuMusic = new ArrayList<URL>();

    public ArrayList<URL> hitSmallMetal = new ArrayList<URL>();
    public ArrayList<URL> hitSmallGround = new ArrayList<URL>();
    public ArrayList<URL> hitSmallWood = new ArrayList<URL>();

    public ArrayList<URL> playerWoundsAudio = new ArrayList<URL>();
    public ArrayList<URL> playerMortarExplosionsAudio = new ArrayList<URL>();
    public ArrayList<URL> weapon_lewis_burst = new ArrayList<URL>();

    public ArrayList<URL> humanUkAttackSound = new ArrayList<URL>();
    public ArrayList<URL> humanUkMoveSound = new ArrayList<URL>();
    public ArrayList<URL> humanUkPainSound = new ArrayList<URL>();

    public URL menu_selection;

    public URL weapon_enfield_shoot;
    public URL weapon_enfield_reload;
    public URL weapon_mortar_shoot;

    public BufferedImage getRandomMapTexture() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(mapTextures.size());
        return mapTextures.get(index);

    }

    public BufferedImage getRandomMapGrassTexture() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(mapTexturesGrassCover.size());
        return mapTexturesGrassCover.get(index);

    }
    private void dumpFolder(File dir, int indent) {
        if (!dir.exists()) {
            System.out.println("".repeat(indent) + dir + " DOES NOT EXIST");
            return;
        }
        System.out.println("".repeat(indent) + dir.getName() + "/");
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) dumpFolder(f, indent+2);
            else System.out.println("".repeat(indent+2) + f.getName());
        }
    }

    private final File texturesRoot = new File(System.getProperty("user.dir"), "resources/images");

    public Resources() {
        File imagesFolder = new File(System.getProperty("user.dir"), "resources");
        loadTextureFolder(texturesRoot);
        try {
            // путь к JAR или к папке с классами
            File codeSource = new File(
                    this.getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
                            .getPath()
            );


            // корень classpath (куда IDEA скопировала resources/)
            URL rootUrl = this.getClass().getResource("/");

            if (codeSource.isFile() && codeSource.getName().endsWith(".jar")) {
                // если запускаем из JAR — сканируем его
                JarDirLoader.LoadAllPNG(codeSource.getAbsolutePath(), allTextures);
                JarDirLoader.LoadAllTXT(codeSource.getAbsolutePath(), allTxts);
            } else {
                // если запускаем из IDEA / из папки classes — рекурсивно обходим resources
//                loadTextureFolder(imagesFolder);
                loadTxtFolder(imagesFolder);
            }
        } catch (Exception e) {
            // на всякий случай (например, проблемы с URI)
//            loadTextureFolder(imagesFolder);
            loadTxtFolder(imagesFolder);
        }

        URL modelUrl;
        try {
            for (int i = 1; i <= 28; i++) {
                modelUrl = this.getClass().getResource("/images/objects/human/uk/death/death_up_" + i + ".png");
                uk_sold_up_deaths.add(ImageIO.read(modelUrl));
            }
            for (int i = 1; i <= 28; i++) {
                modelUrl = this.getClass().getResource("/images/objects/human/ger/death/death_up_" + i + ".png");
                ger_sold_up_deaths.add(ImageIO.read(modelUrl));
            }
            for (int i = 1; i <= 5; i++) {
                modelUrl = this.getClass().getResource("/images/objects/human/uk/death/death_down_" + i + ".png");
                uk_sold_down_deaths.add(ImageIO.read(modelUrl));
            }
            for (int i = 1; i <= 5; i++) {
                modelUrl = this.getClass().getResource("/images/objects/human/ger/death/death_down_" + i + ".png");
                ger_sold_down_deaths.add(ImageIO.read(modelUrl));
            }

            for (int i = 1; i <= 3; i++) {
                modelUrl = this.getClass().getResource("/images/objects/human/blood/blood_" + i + ".png");
                playerBloodImages.add(ImageIO.read(modelUrl));
            }

            for (int i = 0; i <= 2; i++) {
                modelUrl = this.getClass().getResource("/images/objects/tree/tree_broken_" + i + ".png");
                brokenTrees.add(ImageIO.read(modelUrl));
            }

            for (int i = 0; i <= 2; i++) {
                modelUrl = this.getClass().getResource("/images/terrarin/explosions/explosion_" + i + ".png");
                artExplosions.add(ImageIO.read(modelUrl));
            }

            for (int i = 0; i <= 15; i++) {
                modelUrl = this.getClass().getResource("/images/terrarin/grass/grass_" + i + ".png");
                mapTexturesGrassCover.add(ImageIO.read(modelUrl));
            }

            for (int i = 0; i <= 15; i++) {
                modelUrl = this.getClass().getResource("/images/terrarin/trenches/grass_" + i + ".png");
                mapTrenchesCover.add(ImageIO.read(modelUrl));
            }

            modelUrl = this.getClass().getResource("/images/objects/weapon/bullet.png");
            rifleBullet = ImageIO.read(modelUrl);

            modelUrl = this.getClass().getResource("/images/objects/weapon/bullet_flash.png");
            rifleBulletFlash = ImageIO.read(modelUrl);

            modelUrl = this.getClass().getResource("/images/terrarin/dirt.png");
            mapTextures.add(ImageIO.read(modelUrl));
            mapDirt = ImageIO.read(modelUrl);

            modelUrl = this.getClass().getResource("/images/fx/aim_cursor.png");
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            aimCursor = toolkit.createCustomCursor(ImageIO.read(modelUrl), new Point(0, 0), "img");
            normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            crosshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
            noCursor = blankCursor;

            // mortar
            modelUrl = this.getClass().getResource("/images/objects/weapon/mortar_shell.png");
            mortarShell = ImageIO.read(modelUrl);
            modelUrl = this.getClass().getResource("/images/objects/weapon/mortar_flash.png");
            mortarShellFlash = ImageIO.read(modelUrl);
            modelUrl = this.getClass().getResource("/images/objects/weapon/mortar_explosion.png");
            mortarShellExplosion = ImageIO.read(modelUrl);
            modelUrl = this.getClass().getResource("/images/objects/weapon/mortar_explosion_decale.png");
            mortarExplosionSprite = ImageIO.read(modelUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 1; i <= 3; i++) {
                mainMenuMusic.add(this.getClass().getResource("/sounds/music/menu_" + i + ".mp3"));
            }

            menu_selection = this.getClass().getResource("/sounds/fx/menu_selection.wav");

            weapon_enfield_shoot = this.getClass().getResource("/sounds/weapons/enfield/enfield.wav");
            weapon_enfield_reload = this.getClass().getResource("/sounds/weapons/enfield/enfield_reload.wav");

            weapon_mortar_shoot = this.getClass().getResource("/sounds/weapons/mortar_small/mortar_shoot.wav");

            for (int i = 1; i <= 5; i++) {
                weapon_lewis_burst.add(this.getClass().getResource(
                        "/sounds/weapons/lewis_burst/lewis_0" + i + ".wav"));
            }

            for (int i = 1; i <= 3; i++) {
                playerWoundsAudio.add(this.getClass().getResource("/sounds/wounds/" + i + ".wav"));
            }

            for (int i = 1; i <= 4; i++) {
                playerMortarExplosionsAudio.add(this.getClass().getResource(
                        "/sounds/weapons/mortar_small/explosion/0" + i + ".wav"));
            }

            for (int i = 1; i <= 26; i++) {
                hitSmallGround.add(this.getClass().getResource("/sounds/hit/small/ground/hit-" + i + ".wav"));
            }
            for (int i = 1; i <= 15; i++) {
                hitSmallMetal.add(this.getClass().getResource("/sounds/hit/small/metal/hit-" + i + ".wav"));
            }
            for (int i = 1; i <= 10; i++) {
                hitSmallWood.add(this.getClass().getResource("/sounds/hit/small/wood/hit-" + i + ".wav"));
            }

            // HUMAN UK
            for (int i = 1; i <= 11; i++) {
                humanUkAttackSound.add(this.getClass().getResource("/sounds/human/uk/attack/attack-" + i + ".wav"));
            }
            for (int i = 1; i <= 7; i++) {
                humanUkMoveSound.add(this.getClass().getResource("/sounds/human/uk/move/move-" + i + ".wav"));
            }
            for (int i = 1; i <= 9; i++) {
                humanUkPainSound.add(this.getClass().getResource("/sounds/human/uk/pain/pain-" + i + ".wav"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(">>> All texture keys: " + allTextures.keySet());

//        System.out.println(">>> Dumping objects folder:");
//        File objectsRoot = new File(System.getProperty("user.dir"), "resources/images/objects");
//        dumpFolder(objectsRoot, 2);

        System.out.println(">>> Loaded object-texture keys:");
        allTextures.keySet().stream()
                .forEach(k -> System.out.println("   - " + k));
    }

    public BufferedImage getTexture(String textureName) {
        return allTextures.get(textureName);
    }

    public Map<String, BufferedImage> getTextureMap() {
        return allTextures;
    }

    public BufferedReader getTXT(String txtName) {
        return allTxts.get(txtName);
    }

    public Map<String, BufferedReader> getTXTMap() {
        return allTxts;
    }

    private void loadTxtFolder(File f) {

        if (f.exists()) {
            String[] files = f.list();
            
            for (int i = 0; i < files.length; ++i) {
                File currentFile = new File(f.getAbsolutePath() + System.getProperty("file.separator") + files[i]);
                if (currentFile.isDirectory()) {
                    loadTxtFolder(currentFile);
                    continue;
                }
                try {
                    if (currentFile.getName().substring(currentFile.getName().length() - 4, currentFile.getName().length()).equals(".txt")) {
                        String folderName = new File(currentFile.getParent()).getName();
                        allTxts.put(folderName + "/" + currentFile.getName(), new BufferedReader(new FileReader(currentFile)));
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    /** Рекурсивно обходит папку и загружает PNG-файлы */
    private void loadTextureFolder(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                loadTextureFolder(f);
            }
            else if (f.getName().toLowerCase().endsWith(".png")) {
                try {
                    // получаем имя родительской папки
                    String folderName = f.getParentFile().getName();
                    // ключ — "lastFolder/fileName.png"
                    String key = folderName + "/" + f.getName();
                    BufferedImage img = ImageIO.read(f);
                    allTextures.put(key, img);
                    System.out.println("Loaded texture under key → '" + key + "'");
                } catch (IOException ignored) {}
            }
        }
    }

}
