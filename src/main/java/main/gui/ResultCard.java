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
    private final String TEXT_COLOR = "#616161";
    private final String FIRST_TEXT_COLOR = "#212121";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);
    private ImageIcon green = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessNotCrowded.png"));
    private ImageIcon yellow = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessMediumCrowded.png"));
    private ImageIcon red = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessCrowded-100x100.png"));


    public ResultCard(int crowdedness,String busNumber, String date, String departureTime, String busStop, int duration) {
        super();
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        this.setBackground(Color.decode(SECONDARY_COLOR));
        setVisible(true);

        JPanel busIcon = new JPanel();
        busIcon.setLayout(new GridLayout(0, 1));
        busIcon.setPreferredSize(new Dimension (100, 100));

        JLabel busIconLabel = new JLabel();
        busIconLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        this.add(busIcon);

        JPanel pResultTextContainer = new JPanel();
        pResultTextContainer.setLayout(new GridLayout(2,3));

        JLabel pResultNumber = new JLabel(busNumber);
        pResultNumber.setFont(h1);
        pResultNumber.setForeground(Color.decode(FIRST_TEXT_COLOR));

        pResultTextContainer.add(pResultNumber);

        JLabel dateContainer = new JLabel(date);
        dateContainer.setFont(h3);
        dateContainer.setForeground(Color.decode(TEXT_COLOR));

        pResultTextContainer.add(dateContainer);



        JTextArea pResultInfo = new JTextArea("Departure " + departureTime + ", " + busStop+ ", \n" + duration + " min.");
        pResultInfo.setFont(h3);
        pResultInfo.setForeground(Color.decode(FIRST_TEXT_COLOR));
        pResultTextContainer.setBackground(Color.decode(SECONDARY_COLOR));
        pResultTextContainer.setPreferredSize(new Dimension(300, 150));

        pResultTextContainer.add(pResultInfo);
        this.add(pResultTextContainer);

        busIconLabel.setIcon(crowdedness(crowdedness));
        busIcon.add(busIconLabel);

        busIcon.setBackground(Color.decode(SECONDARY_COLOR));

        JPanel pDivider = new JPanel();
        pDivider.setPreferredSize(new Dimension(CARD_WIDTH, 15));

        }


    public ImageIcon crowdedness(int crowdedness){

        ImageIcon crowdednessIcon;

        if (crowdedness >= 75){
            crowdednessIcon = red;

        }else if (crowdedness >= 35){
            crowdednessIcon = yellow;

        }else {
            crowdednessIcon = green;

        }
        return crowdednessIcon;
        }
    }




