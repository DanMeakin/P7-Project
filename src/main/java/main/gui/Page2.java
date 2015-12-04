package main.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

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
        pBackground.setBackground(Color.decode("B2DFDB"));
        pBackground.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        pBackground.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        //Topbar
        JPanel pTop = new JPanel();
        pTop.setPreferredSize(new Dimension(SCREEN_WIDTH, 70));
        pTop.setBackground(Color.decode("BDFDB"));
        pTop.setLayout(new FlowLayout(FlowLayout.LEADING,0,10));
        JMenuBar topMenuBar = new JMenuBar();

        //Topbar - Menu/button
        JMenu topMenu = new JMenu("");
        ImageIcon topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        topMenu.setIcon(topMenuButtonIcon);
        topMenu.setOpaque(true);
        topMenu.setBackground(Color.decode(PRIMARY_COLOR));
        topMenu. setBorder(BorderFactory.createLineBorder(Color.decode(PRIMARY_COLOR), 6));

        JMenu
    }


}
