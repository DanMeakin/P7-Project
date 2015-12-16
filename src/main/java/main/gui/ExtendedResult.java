package main.gui;

import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 * Created by Helle on 16-12-2015.
 */


public class ExtendedResult extends JPanel{

    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;
    private final String PRIMARY_COLOR = "#009688";
    private final String SECONDARY_COLOR = "#FAFAFA";
    private final String TERTIARY_COLOR = "#D2D4D2";
    private final String TEXT_COLOR = "#616161";
    private final String FIRST_TEXT_COLOR = "#212121";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);

    public ExtendedResult(){
         super();
        //this.setBackground(Color.decode(SECONDARY_COLOR));
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        JPanel pContentContainer = new JPanel();
        //pContentContainer.setPreferredSize();
        pContentContainer.setLayout(new CardLayout(15,0)); // sets hGap and vGap to 15 each

        // Content
        JPanel pContent = new JPanel();
        pContent.setLayout(new FlowLayout(FlowLayout.LEADING));
        pContent.setBackground(Color.decode("#FAFAFA"));
        pContent.setPreferredSize(new Dimension(SCREEN_WIDTH - 30, SCREEN_HEIGHT - 150));
        DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowSize(5);
        shadow.setShadowColor(Color.BLACK);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);
        pContent.setBorder(shadow);

        pContentContainer.add(pContent);
        this.add(pContentContainer);

    }



}
