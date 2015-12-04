package main.gui;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;
import com.sun.scenario.Settings;
import javafx.animation.Timeline;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Helle on 02-12-2015.
 */


public class StartScreen extends JFrame {

    // Constants
    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;
    private final String PRIMARY_COLOR = "#009688";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);

    // Private members
    private JPanel startPage;

    public static void main (String args[]){
        new StartScreen();
    }

    public StartScreen(){
        super ("StartScreen");
        setSize (new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        startPage = getStartPage();
        add(startPage);

        pack();
        setVisible(true);
    }

    private JPanel getStartPage() {
        // Background
        JPanel pBackground = new JPanel ();
        pBackground.setBackground(Color.decode("#B2DFDB"));
        pBackground.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        pBackground.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));

        // Topbar
        JPanel pTop = new JPanel ();
        pTop.setPreferredSize(new Dimension(SCREEN_WIDTH,70));
        pTop.setBackground(Color.decode(PRIMARY_COLOR));
        pTop.setLayout(new FlowLayout(FlowLayout.LEADING,0,10));

        /*// Topbar - MenuButton GAMMEL
        ImageIcon topMenuButtonIcon;
        JButton topMenuButton;
        topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        topMenuButton = new JButton(topMenuButtonIcon);
        topMenuButton.setPreferredSize(new Dimension(50,50));
        topMenuButton.setBackground(Color.decode(PRIMARY_COLOR));
        topMenuButton.setBorder(BorderFactory.createLineBorder(Color.decode(PRIMARY_COLOR), 10));
        topMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Teksten nedenunder fjerner alt indhold og tilføjer nyt
                // changeScreen(getPage2());

                openMenu();

            }
        });*/

        JMenuBar topMenuBar = new JMenuBar();


        // Topbar - Menu/Button
        JMenu topMenu = new JMenu("");
        ImageIcon topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        topMenu.setIcon(topMenuButtonIcon);
        topMenu.setOpaque(true);
        topMenu.setBackground(Color.decode(PRIMARY_COLOR));
        topMenu.setBorder(BorderFactory.createLineBorder(Color.decode(PRIMARY_COLOR),6));

        JMenuItem newSearch = new JMenuItem("New Search");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem about = new JMenuItem("About");
        JMenuItem exit = new JMenuItem("Exit");

        newSearch.addActionListener(new ActionListener() {
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
        });


        topMenu.add(newSearch);
        topMenu.add(settings);
        topMenu.add(about);
        topMenu.add(exit);
        topMenuBar.add(topMenu);
        pTop.add(topMenuBar);

        // Topbar - App Name Label
        JLabel appNameLabel = new JLabel("Compute My Commute");
        appNameLabel.setFont(h1);
        appNameLabel.setForeground(Color.white);

        // Content Container
        JPanel pContentContainer = new JPanel();
        pContentContainer.setLayout(new CardLayout(15,15));


        // Content
        JPanel pContent = new JPanel();
        pContent.setLayout(new FlowLayout(FlowLayout.LEADING));
        pContent.setBackground(Color.decode("#FAFAFA"));
        pContent.setPreferredSize(new Dimension(SCREEN_WIDTH - 30, SCREEN_HEIGHT - 100));
        DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowSize(5);
        shadow.setShadowColor(Color.BLACK);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);
        pContent.setBorder(shadow);
        pContentContainer.add(pContent);

        // Row Panels
        JPanel fromContainer = new JPanel();
        fromContainer.setPreferredSize(new Dimension(430,80));

        // From: Label
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(h2);
        fromLabel.setForeground(Color.decode("#616161"));
        fromContainer.add(fromLabel);

        JPanel destinationContainer = new JPanel();
        destinationContainer.setPreferredSize(new Dimension(430,80));

        JPanel dateContainer = new JPanel();
        dateContainer.setPreferredSize(new Dimension(430,160));

        JPanel crowdedContainer = new JPanel();
        crowdedContainer.setPreferredSize(new Dimension(430,240));

        JPanel searchContainer = new JPanel();
        searchContainer.setPreferredSize(new Dimension(430, 70));

        // Find Bus Button
        JButton findBusButton = new JButton("FIND BUS");
        findBusButton.setForeground(Color.decode(PRIMARY_COLOR));
        findBusButton.setBackground(Color.decode("#FAFAFA"));
        findBusButton.setBorder(BorderFactory.createLineBorder(Color.decode("#FAFAFA")));
        findBusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Teksten nedenunder fjerner alt indhold og tilføjer nyt
                changeScreen(getPage2());
            }
        });
        searchContainer.add(findBusButton);


        // Sidste add
        pContent.add(fromContainer);
        pContent.add(destinationContainer);
        pContent.add(dateContainer);
        pContent.add(crowdedContainer);
        pContent.add(searchContainer);

        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode("#FAFAFA"));

        }


        pTop.add(appNameLabel);
        pBackground.add(pTop);
        pBackground.add(pContentContainer);

        return pBackground;
    }


    private JPanel getPage2() {
        JPanel page2 = new JPanel();

        //TODO: Create components from page 2 and add to JPanel
        page2.setBackground(Color.black);
        page2.setPreferredSize(new Dimension(100,100));


        return page2;
    }
    private void changeScreen (JPanel panelToChangeTo){
        getContentPane().removeAll();
        getContentPane().add(panelToChangeTo);
        getContentPane().doLayout();
        update(getGraphics());
    }
    private void openMenu (){

    }

}


