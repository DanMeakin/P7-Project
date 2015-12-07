package main.gui;

import org.jdesktop.swingx.border.DropShadowBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by janusalarsen on 04/12/2015.
 */
public class Page2 extends JFrame{
    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;
    private final String PRIMARY_COLOR = "#009688";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);
    private JPanel page2;
    //private ImageIcon topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));

    public static void main (String[] args){
        new Page2();
    }
    public Page2(){
        super("Page2");
        setSize (new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        page2 = getPage2();
        add(page2);


        pack();
        setVisible(true);

    }
    private JPanel getPage2(){
        //Background
        JPanel pBackground = new JPanel();
        pBackground.setBackground(Color.decode("#B2DFDB"));
        pBackground.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        pBackground.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        //Topbar
        JPanel pTop = new JPanel();
        pTop.setPreferredSize(new Dimension(SCREEN_WIDTH, 70));
        pTop.setBackground(Color.decode(PRIMARY_COLOR));
        pTop.setLayout(new FlowLayout(FlowLayout.LEADING,0,10));
        JMenuBar topMenuBar = new JMenuBar();
        topMenuBar.setBackground(Color.decode(PRIMARY_COLOR));

        //Topbar - Menu/button
        JMenu topMenu = new JMenu();
        ImageIcon topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        topMenu.setBackground(Color.decode(PRIMARY_COLOR));
        topMenu.setIcon(topMenuButtonIcon);
       // topMenu.setContentAreaFilled(true);
       // topMenu.setBorderPainted(true);
        topMenu.setOpaque(true);

        topMenu.setBorder(BorderFactory.createLineBorder(Color.decode(PRIMARY_COLOR),6));

        JMenuItem newSearch = new JMenuItem("New Search");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem about = new JMenuItem("About");
        JMenuItem exit = new JMenuItem("Exit");

        newSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(getPage2());
                /*this will need to go to the
                *startPage() instead of the Page2 */

                repaint();

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

        //Topbar - App Name Label
        JLabel appNameLabel = new JLabel("Compute My Compute");
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


        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode("#FAFAFA"));

        }

        //results
        JPanel pResult = new JPanel();
        pResult.setLayout(new FlowLayout(FlowLayout.LEADING));
        pResult.setBackground(Color.decode(PRIMARY_COLOR));
        pResult.setPreferredSize((new Dimension(SCREEN_WIDTH -50, 150)));

        DropShadowBorder resultShadow = new DropShadowBorder();
        shadow.setShadowSize(5);
        shadow.setShadowColor(Color.BLACK);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);
        pResult.setBorder(resultShadow);
        JLabel pResultText = new JLabel("Bus Results");
        pResultText.setFont(h2);
        pResultText.setForeground(Color.white);
        pResult.add(pResultText);

        pTop.add(appNameLabel);
        pBackground.add(pTop);
        pBackground.add(pContentContainer);
        pContent.add(pResult);


        setVisible(true);
        return pBackground;
    }
    private void changeScreen (JPanel panelToChangeTo) {
            getContentPane().removeAll();
            getContentPane().add(panelToChangeTo);
            getContentPane().doLayout();
            update(getGraphics());
    }
    private void openMenu(){

    }
}

