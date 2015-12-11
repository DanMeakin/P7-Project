package main.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by janusalarsen on 10/12/2015.
 */
public class ResultCard extends JPanel {
    private final int CARD_HEIGHT = 150;
    private final int CARD_WIDTH = 430;
    private final String SECONDARY_COLOR = "#FAFAFA";
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);
    private ImageIcon green = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessNotCrowded.png"));
    private ImageIcon yellow = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessMediumCrowded.png"));
    private ImageIcon red = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessCrowded-100x100.png"));


    public ResultCard(int crowdedness) {
        super();
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        this.setBackground(Color.decode(SECONDARY_COLOR));
        setVisible(true);

        JPanel busIcon = new JPanel();
        busIcon.setLayout(new GridLayout(0, 1));
        busIcon.setPreferredSize(new Dimension (100, 100));

        JLabel busIconLabel = new JLabel();
        this.add(busIcon);

        busIconLabel.setIcon(crowdedness(crowdedness));
        busIcon.add(busIconLabel);

        busIcon.setBackground(Color.decode(SECONDARY_COLOR));

       /* JPanel pResultTextContainer = new JPanel();
        pResultTextContainer.setLayout(new GridLayout(2, 2));

        JLabel pResultNumber = new JLabel("2C                Monday, 30.11.2015");
        pResultNumber.setFont(h2);
        pResultNumber.setForeground(Color.BLACK);
        pResultTextContainer.add(pResultNumber);
        JLabel pResultInfo = new JLabel("Depature 15:10, BornHolmsgade (Aalborg), 39 min.");
        pResultInfo.setFont(h3);
        pResultInfo.setForeground(Color.BLACK);
        pResultTextContainer.setBackground(Color.decode(SECONDARY_COLOR));
        pResultTextContainer.setPreferredSize(new Dimension(300, 150));
        pResultTextContainer.add(pResultInfo);
        this.add(pResultTextContainer);*/

        JPanel pDivider = new JPanel();
        pDivider.setPreferredSize(new Dimension(CARD_WIDTH, 15));

        }
        public ImageIcon crowdedness(int crowdedness){

        ImageIcon crowdednessIcon;

        if (crowdedness >= 75){
            crowdednessIcon = red;
            JPanel pResultTextContainer = new JPanel();

            pResultTextContainer.setLayout(new GridLayout(2, 2));
            JLabel pResultNumber = new JLabel("2C                Monday, 30.11.2015");
            pResultNumber.setFont(h2);
            pResultNumber.setForeground(Color.BLACK);
            pResultTextContainer.add(pResultNumber);
            JLabel pResultInfo = new JLabel("Depature 15:10, BornHolmsgade (Aalborg), 39 min.");
            pResultInfo.setFont(h3);
            pResultInfo.setForeground(Color.BLACK);
            pResultTextContainer.setBackground(Color.decode(SECONDARY_COLOR));
            pResultTextContainer.setPreferredSize(new Dimension(300, 150));
            pResultTextContainer.add(pResultInfo);
            this.add(pResultTextContainer);
        }else if (crowdedness >= 35){
            crowdednessIcon = yellow;

            JPanel pResultTextContainer1 = new JPanel();
            pResultTextContainer1.setLayout(new GridLayout(2, 2));
            JLabel pResultNumber1 = new JLabel("2B                Monday, 30.11.2015");
            pResultNumber1.setFont(h2);
            pResultNumber1.setForeground(Color.BLACK);
            pResultTextContainer1.add(pResultNumber1);
            JLabel pResultInfo1 = new JLabel("Depature 15:15, BornHolmsgade (Aalborg), 40 min.");
            pResultInfo1.setFont(h3);
            pResultInfo1.setForeground(Color.BLACK);
            pResultTextContainer1.setBackground(Color.decode(SECONDARY_COLOR));
            pResultTextContainer1.setPreferredSize(new Dimension(300, 150));
            pResultTextContainer1.add(pResultInfo1);
            this.add(pResultTextContainer1);
        }else {
            crowdednessIcon = green;

            JPanel pResultTextContainer2 = new JPanel();
            pResultTextContainer2.setLayout(new GridLayout(2, 2));
            JLabel pResultNumber2 = new JLabel("2E                Monday, 30.11.2015");
            pResultNumber2.setFont(h2);
            pResultNumber2.setForeground(Color.BLACK);
            pResultTextContainer2.add(pResultNumber2);
            JLabel pResultInfo = new JLabel("Depature 15:20, BornHolmsgade (Aalborg), 45 min.");
            pResultInfo.setFont(h3);
            pResultInfo.setForeground(Color.BLACK);
            pResultTextContainer2.setBackground(Color.decode(SECONDARY_COLOR));
            pResultTextContainer2.setPreferredSize(new Dimension(300, 150));
            pResultTextContainer2.add(pResultInfo);
            this.add(pResultTextContainer2);
        }
        return crowdednessIcon;
        }
    }




