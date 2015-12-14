package main.gui;

import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Helle on 08-12-2015.
 */
public class TopBar extends JPanel {
    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;
    public static final String PRIMARY_COLOR = "#009688";
    private final String TEXT_COLOR = "#616161";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);
    public JMenuItem newSearch;
    public JMenuItem settings;
    public JMenuItem about;
    public JMenuItem exit;

    public TopBar (){
        // Topbar
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,70));
        this.setBackground(Color.decode(PRIMARY_COLOR));
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,10));

        // Topbar - JMenuBar
        JMenuBar topMenuBar = new JMenuBar();

        // JMenubar - Menu/Button
        JMenu topMenu = new JMenu("");
        ImageIcon topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        topMenu.setIcon(topMenuButtonIcon);
        topMenu.setOpaque(true);
        topMenu.setBackground(Color.decode(PRIMARY_COLOR));
        topMenu.setBorder(BorderFactory.createLineBorder(Color.decode(PRIMARY_COLOR),6));

        newSearch = new JMenuItem("New Search");
        settings = new JMenuItem("Settings");
        about = new JMenuItem("About");
        exit = new JMenuItem("Exit");

 /*       newSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(getStartPage());
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });*/

        topMenu.add(newSearch);
        topMenu.add(settings);
        topMenu.add(about);
        topMenu.add(exit);
        topMenuBar.add(topMenu);
        this.add(topMenuBar);

        // Topbar - App Name Label
        JLabel appNameLabel = new JLabel("Compute My Commute");
        appNameLabel.setFont(h1);
        appNameLabel.setForeground(Color.white);
        this.add(appNameLabel);
    }
}
