package gameframework;

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window extends JFrame {
    private static final long serialVersionUID = -6017798704092079083L;
    private static final int WINDOW_SIZE_X = 1024;
    private static final int WINDOW_SIZE_Y = 640;
    private static final int WINDOW_BORDER_SIZE_X = 8;
    private static final int WINDOW_BORDER_SIZE_Y = 31;

    @SuppressWarnings("unused")
    Window() {
        this.setTitle("Behind The Trenches [Tech. Demo]");
        if (true) // Fullscreen
        {
            this.setUndecorated(true);
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else // Window
        {
            this.setSize(1350, 768);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
        }

        // Exit when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(new Framework());
        this.setVisible(true);

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });

    }

    public static int getWindowSizeY() {
        return WINDOW_SIZE_Y;
    }

    public static int getWindowSizeX() {
        return WINDOW_SIZE_X;
    }

    public static int getWindowBorderSizeX() {
        return WINDOW_BORDER_SIZE_X;
    }

    public static int getWindowBorderSizeY() {
        return WINDOW_BORDER_SIZE_Y;
    }
}
