import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CompletableFuture;

/**
 * Container for all other GUI elements.
 */
class MainFrame extends JFrame {
    private GameSettings gameSettings = GameSettings.getInstance();
    private AudioManager audioManager = AudioManager.getInstance();
    private Dimension dimension = new Dimension(1280, 800);

    @SuppressWarnings("SpellCheckingInspection")
    MainFrame() {
        super("Random Chess");

        gameSettings.initDefaults();


        setSize(dimension);
        setMinimumSize(new Dimension(1100, 800));
        setLocationRelativeTo(null); //Centers the window in the middle of the main screen
        getContentPane().setBackground(ColorGenerator.backgroundColor);

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());

        ImageManager imageManager = ImageManager.getInstance();

        //Set window icon
        ImageIcon icon = imageManager.getScaledImage("pawn_blue");
        setIconImage(icon.getImage());

        OptionPanel mainMenuPanel = new MainMenuPanel();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(mainMenuPanel, c);

        ImageButton fullScreenButton = new FullScreenButton(e -> toggleFullScreen());

        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.ipadx = 10;
        c.ipady = 10;
        c.anchor = GridBagConstraints.NORTHEAST;
        add(fullScreenButton, c);

        ImageButton infoButton = new HelpButton();
        infoButton.addActionListener(e -> {
            ImageIcon logoIcon = ImageManager.getInstance().getScaledImage("pawn_blue", 35, 35, 0);
            JOptionPane.showMessageDialog(this, "Random Chess is made by:\nJack Xiao\nTaylor" +
                            " Schmidt\nBrandon Cecchini\nRyan Byrnes\nMackenzie Dahlem\nBenjamin Phillips" +
                            "\nand \nChristopher DeLuca.\n\n\n" +
                            "Audio Credits:\n" +
                            "\"annabloom_click1\" by annabloom of Freesound.org\n" +
                            "\"Explosion, 8-bit, 01.wav\" by InspectorJ (www.jshaw.co.uk) of Freesound.org\n" +
                            "\"aiwha_explosion.wav\" by Aiwah of Freesound.org\n" +
                            "Royalty Free Music from Bensound", "About this game",
                    JOptionPane.INFORMATION_MESSAGE, logoIcon);
        });

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.ipadx = 10;
        c.ipady = 10;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(infoButton, c);


        InfoPanel infoPanel = new InfoPanel("white");
        infoPanel.setVisible(false);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.ipadx = 10;
        c.ipady = 10;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(infoPanel, c);

        GamePanel gamePanel = new GamePanel();
        gamePanel.setVisible(false);
        gamePanel.addTurnChangeListener(e -> infoPanel.toggleColor());
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(gamePanel, c);

        PausePanel pausePanel = new PausePanel();
        pausePanel.setVisible(false);


        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(pausePanel, c);

        PauseButton pauseButton = new PauseButton();
        ActionListener pauseListener = e -> {
            gamePanel.setVisible(!gamePanel.isVisible());
            pausePanel.setVisible(!pausePanel.isVisible());
            infoPanel.setVisible(!infoPanel.isVisible());
            pauseButton.toggle();
        };
        pauseButton.addActionListener(pauseListener);
        pauseButton.setVisible(false);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.ipadx = 10;
        c.ipady = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        add(pauseButton, c);

        MuteButton muteButton = new MuteButton();
        if ((boolean) gameSettings.get(GameSettings.MUTED)) {
            muteButton.toggle();
        }
        muteButton.addActionListener(e -> {
            boolean isMuted = (boolean) gameSettings.get(GameSettings.MUTED);
            gameSettings.put(GameSettings.MUTED, !isMuted);
            muteButton.toggle();
            if (isMuted) {
                audioManager.playMusic();
            } else {
                audioManager.stopMusic();
            }
        });

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.ipadx = 0;
        c.ipady = 10;
        c.anchor = GridBagConstraints.NORTHEAST;
        add(muteButton, c);

        //Continue button
        pausePanel.getButton(0).addActionListener(pauseListener);
        //New game button
        pausePanel.getButton(1).addActionListener(e -> {
            if (infoPanel.getColor().equals(InfoPanel.BLACK)) {
                infoPanel.toggleColor();
            }
            String color = gamePanel.setUpBoard(true);
            if (!infoPanel.getColor().equals(color)){
                infoPanel.toggleColor();
            }
            gamePanel.setVisible(!gamePanel.isVisible());
            pausePanel.setVisible(!pausePanel.isVisible());
            infoPanel.setVisible(!infoPanel.isVisible());
            pauseButton.toggle();
        });
        //Quit button
        pausePanel.getButton(2).addActionListener(e -> {
            dispose();
            gamePanel.save();
            System.exit(0);
        });

        //Continue button
        mainMenuPanel.getButton(0).addActionListener(e -> CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(80);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String color = gamePanel.setUpBoard(false);
            if (!infoPanel.getColor().equals(color)){
                infoPanel.toggleColor();
            }
            mainMenuPanel.setVisible(false);
            pauseButton.setVisible(true);
            infoPanel.setVisible(true);
            gamePanel.setVisible(true);
            validate();
        }));
        //New game button
        mainMenuPanel.getButton(1).addActionListener(e -> {
            String color = gamePanel.setUpBoard(true);
            if (!infoPanel.getColor().equals(color)){
                infoPanel.toggleColor();
            }
            mainMenuPanel.setVisible(false);
            pauseButton.setVisible(true);
            infoPanel.setVisible(true);
            gamePanel.setVisible(true);
            validate();
        });
        //Quit button
        mainMenuPanel.getButton(2).addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        if (gamePanel.loadGame() == null) {
            mainMenuPanel.getButton(0).setVisible(false);
        }

        audioManager.playMusic();

        //Enter fullscreen if the game was closed in fullscreen.
        boolean isFullscreen = (boolean) gameSettings.get(GameSettings.FULLSCREEN);
        if (isFullscreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            fullScreenButton.toggle();
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
//                int confirm = JOptionPane.showOptionDialog(
//                        null, "Are you sure you want to quit?",
//                        "Quit", JOptionPane.YES_NO_OPTION,
//                        JOptionPane.QUESTION_MESSAGE, null, null, null);
//                if (confirm == 0) {
//                    gamePanel.save();
//                    System.exit(0);
//                }
                audioManager.stopMusic();
                gamePanel.save();
                System.exit(0);
            }
        };
        addWindowListener(exitListener);

        setVisible(true);
    }

    private void toggleFullScreen() {
        dispose();
        if ((boolean) gameSettings.get(GameSettings.FULLSCREEN)) {
            setUndecorated(false);
            setExtendedState(JFrame.NORMAL);
            setSize(dimension);
            setLocationRelativeTo(null);
        } else {
            //Stores size of window so that it can be reloaded if the window is no longer full screen.
            dimension.height = getHeight();
            dimension.width = getWidth();

            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
        }
        setVisible(true);
    }

    public class FullScreenButton extends ImageButton {
        FullScreenButton(ActionListener actionListener) {
            super();
            ImageManager m = ImageManager.getInstance();
            icon = m.getScaledImage("full_screen_icon");
            iconPush = m.getScaledImage("full_screen_icon_push");
            altIcon = m.getScaledImage("exit_full_screen_icon");
            altIconPush = m.getScaledImage("exit_full_screen_icon_push");
            hasAlt = true;

            updateIcon();

            GameSettings gameSettings = GameSettings.getInstance();

            addActionListener(e -> {
                audioManager.playClick();
                if (gameSettings.get(GameSettings.FULLSCREEN) != null) {
                    actionListener.actionPerformed(new ActionEvent(this, 1,
                            Boolean.toString((boolean) gameSettings.get(GameSettings.FULLSCREEN))));

                    toggle();
                    gameSettings.put(GameSettings.FULLSCREEN, !(boolean) gameSettings.get(GameSettings.FULLSCREEN));
                }
            });
        }
    }

    private class HelpButton extends ImageButton {
        HelpButton() {
            super();
            ImageManager m = ImageManager.getInstance();
            icon = m.getScaledImage("question_mark_icon");
            iconPush = m.getScaledImage("question_mark_icon_push");

            updateIcon();

            addActionListener(e -> audioManager.playClick());
        }
    }

    private class PauseButton extends ImageButton {
        PauseButton() {
            super();
            ImageManager m = ImageManager.getInstance();
            icon = m.getScaledImage("pause_button");
            iconPush = m.getScaledImage("pause_button_push");
            altIcon = m.getScaledImage("play_button");
            altIconPush = m.getScaledImage("play_button_push");
            hasAlt = true;

            updateIcon();

            addActionListener(e -> audioManager.playClick());
        }
    }

    private class MuteButton extends ImageButton {
        MuteButton() {
            super();
            ImageManager m = ImageManager.getInstance();
            icon = m.getScaledImage("volume_mute_button");
            iconPush = m.getScaledImage("volume_mute_button_push");
            altIcon = m.getScaledImage("volume_on_button");
            altIconPush = m.getScaledImage("volume_on_button_push");
            hasAlt = true;

            updateIcon();

            addActionListener(e -> audioManager.playClick());
        }
    }
}
