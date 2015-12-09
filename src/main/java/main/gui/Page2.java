package main.gui;

import org.jdesktop.swingx.border.DropShadowBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;

/**
 * Created by janusalarsen on 04/12/2015.
 */
public class Page2 extends JPanel{
    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;
    private final String PRIMARY_COLOR = "#009688";
    private final String SECONDARY_COLOR = "#FAFAFA";
    private final String TERTIARY_COLOR = "#D2D4D2";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);

    /*public static void main (String[] args){
        new Page2();
    }*/
    public Page2(){
        //Background
        super();
        this.setBackground(Color.decode("#B2DFDB"));
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        /*//Topbar
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
                *//*this will need to go to the
                *startPage() instead of the Page2 *//*

                repaint();

            }
        });

        topMenu.add(newSearch);
        topMenu.add(settings);
        topMenu.add(about);
        topMenu.add(exit);
        topMenuBar.add(topMenu);
        pTop.add(topMenuBar);*/

        //Topbar - App Name Label
        /*JLabel appNameLabel = new JLabel("Compute My Compute");
        appNameLabel.setFont(h1);
        appNameLabel.setForeground(Color.white);*/

        //Search Container
        JPanel pSearchContainer = new JPanel();
        pSearchContainer.setLayout(new CardLayout(15, 0));

        //Search Content
        JPanel pSearchContent = new JPanel();
        pSearchContent.setLayout(new GridLayout(1, 3));

        pSearchContent.setPreferredSize(new Dimension(SCREEN_WIDTH -30, SCREEN_HEIGHT -720));
        pSearchContent.setBackground(Color.decode(SECONDARY_COLOR));
        DropShadowBorder searchShadow = new DropShadowBorder();
        searchShadow.setShadowSize(5);
        searchShadow.setShadowColor(Color.BLACK);
        searchShadow.setShowLeftShadow(true);
        searchShadow.setShowRightShadow(true);
        searchShadow.setShowBottomShadow(true);
        searchShadow.setShowTopShadow(false);
        pSearchContent.setBorder(searchShadow);
        pSearchContainer.add(pSearchContent);

        for (int i = 0; i < pSearchContainer.getComponents().length; i++) {
            pSearchContainer.getComponent(i).setBackground(Color.decode(SECONDARY_COLOR));
        }

        //Search Container - From Textfield
        JLabel fromLabel = new JLabel ("From");
        pSearchContent.add(fromLabel);
        fromLabel.setFont(h2);

        //Search Container - Icon
        JLabel Icon = new JLabel ("*IMAGE ICON*");
        pSearchContent.add(Icon);

        //Search Container - To Textfield
        JLabel toLabel = new JLabel ("To");
        pSearchContent.add(toLabel);
        toLabel.setFont(h2);

        // Content Container
        JPanel pContentContainer = new JPanel();
        pContentContainer.setLayout(new CardLayout(15, 7));

        // Content
        JPanel pContent = new JPanel();
        pContent.setLayout(new FlowLayout(FlowLayout.LEADING));
        pContent.setBackground(Color.decode(TERTIARY_COLOR));
        pContent.setPreferredSize(new Dimension(SCREEN_WIDTH - 30, SCREEN_HEIGHT - 100));
        DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowSize(5);
        shadow.setShadowColor(Color.BLACK);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);
        pContent.setBorder(shadow);


        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode(SECONDARY_COLOR));

        }

        //results
        JPanel pResult = new JPanel();
        pResult.setLayout(new BoxLayout(pResult, BoxLayout.Y_AXIS));
        pResult.setBackground(Color.decode(SECONDARY_COLOR));
        pResult.setPreferredSize((new Dimension(SCREEN_WIDTH -40, 150)));
        JLabel pResultText = new JLabel("Bus Result");
        pResultText.setFont(h2);
        pResultText.setForeground(Color.BLACK);

        pResultText.setHorizontalTextPosition(JLabel.RIGHT);
        pResultText.setVerticalTextPosition(JLabel.BOTTOM);
        pResult.add(pResultText);

        JPanel pResult1 = new JPanel();
        pResult1.setLayout(new BoxLayout(pResult1, BoxLayout.Y_AXIS));
        pResult1.setBackground(Color.decode(SECONDARY_COLOR));
        pResult1.setPreferredSize((new Dimension(SCREEN_WIDTH -40, 150)));
        JLabel pResultText1 = new JLabel("Second bus Result");
        pResultText1.setFont(h2);
        pResultText1.setForeground(Color.BLACK);
        pResult1.add(pResultText1);

        JPanel pResult2 = new JPanel();
        pResult2.setLayout(new BoxLayout(pResult2, BoxLayout.Y_AXIS));
        pResult2.setBackground(Color.decode(SECONDARY_COLOR));
        pResult2.setPreferredSize((new Dimension(SCREEN_WIDTH -40, 150)));
        JLabel pResultText2 = new JLabel("Third bus Result");
        pResultText2.setFont(h2);
        pResultText2.setForeground(Color.BLACK);
        pResult2.add(pResultText2);

        pContentContainer.add(pContent);
        //pTop.add(appNameLabel);
        //this.add(pTop);
        this.add(pSearchContainer);
        this.add(pContentContainer);

        pContent.add(pResult);
        pContent.add(pResult1);
        pContent.add(pResult2);

        setVisible(true);
    }
}



