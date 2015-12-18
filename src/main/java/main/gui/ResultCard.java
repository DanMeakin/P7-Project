package main.gui;

import main.capacitytracker.CapacityCalculator;
import main.routeplanner.Itinerary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 * Created by janusalarsen on 10/12/2015.
 */
public class ResultCard extends JPanel {

    private final int CARD_HEIGHT = 175;

    private final int CARD_WIDTH = 430;
    public static final String PRIMARY_COLOR = "#009688";
    private final String SECONDARY_COLOR = "#FAFAFA";
    private final String TEXT_COLOR = "#616161";
    private final String FIRST_TEXT_COLOR = "#212121";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);
    private ImageIcon green = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessNotCrowded.png"));
    private ImageIcon yellow = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessMediumCrowded.png"));
    private ImageIcon red = new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessCrowded-100x100.png"));

    private Itinerary itinerary;

    public ResultCard(CapacityCalculator.CrowdednessIndicator crowdednessIndicator, String busNumber, String date, String departureTime, String busStop, int duration, Itinerary itinerary) {
        super();
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        this.setBackground(Color.decode(SECONDARY_COLOR));
        setVisible(true);

        this.itinerary = itinerary;

        // Bus icon container
        JPanel busIcon = new JPanel();
        busIcon.setBackground(Color.decode(SECONDARY_COLOR));
        busIcon.setLayout(new GridLayout(0, 1));
        busIcon.setPreferredSize(new Dimension (105, 100));

        // Bus Icon Label
        JLabel busIconLabel = new JLabel();
        busIcon.setBackground(Color.decode(SECONDARY_COLOR));
        busIconLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        // Results container, which contains all text on the card
        JPanel pResultTextContainer = new JPanel();
        pResultTextContainer.setBackground(Color.decode(SECONDARY_COLOR));
        pResultTextContainer.setPreferredSize(new Dimension(300,120));

        pResultTextContainer.add(busIcon);

        // Container that contains the bus number label
        JPanel resultNumberContainer = new JPanel();
        resultNumberContainer.setBackground(Color.decode(SECONDARY_COLOR));

        pResultTextContainer.add(resultNumberContainer);

        // Label that contains the bus number
        JLabel pResultNumber = new JLabel(busNumber);
        pResultNumber.setPreferredSize(new Dimension(30,30));
        pResultNumber.setFont(h1);
        pResultNumber.setForeground(Color.decode(FIRST_TEXT_COLOR));

        resultNumberContainer.add(pResultNumber);

        // Panel that separates busnumber and date
        JPanel numberSpacer = new JPanel();
        numberSpacer.setBackground(Color.decode(SECONDARY_COLOR));
        numberSpacer.setPreferredSize(new Dimension(80,30));

        pResultTextContainer.add(numberSpacer);

        // Container that contains the label with date of the search
        JPanel dateLabelContainer = new JPanel();
        dateLabelContainer.setBackground(Color.decode(SECONDARY_COLOR));


        pResultTextContainer.add(dateLabelContainer);

        // Label that contains the date of the search
        JLabel dateLabel = new JLabel(date);
        dateLabel.setPreferredSize(new Dimension(150, 30));
        dateLabel.setFont(h3);
        dateLabel.setForeground(Color.decode(TEXT_COLOR));

        dateLabelContainer.add(dateLabel);

        // Container that contains the results text area
        JPanel resultInfoContainer = new JPanel();
        resultInfoContainer.setBackground(Color.decode(SECONDARY_COLOR));

        pResultTextContainer.add(resultInfoContainer);

             // Text area That contains the results: Departure time, start busstop and the travel duration.
        // TODO: Add how many shifts the user needs to get to the destination
        JTextArea pResultInfo = new JTextArea("Departure " + departureTime + ", \n"  + busStop+ ", \n" + duration + " min.");
        pResultInfo.setPreferredSize(new Dimension(280,100));
        pResultInfo.setEditable(false);
        pResultInfo.setFont(h3);
        pResultInfo.setBackground(Color.decode(SECONDARY_COLOR));
        pResultInfo.setForeground(Color.decode(FIRST_TEXT_COLOR));

        resultInfoContainer.add(pResultInfo);

        // Spacer for details button
        JPanel detailsSpacer = new JPanel();
        detailsSpacer.setPreferredSize(new Dimension(280,20));
        detailsSpacer.setBackground(Color.decode(SECONDARY_COLOR));

        // Container for button that enables the user to go to travel details
        JPanel extendResultsContainer = new JPanel();
        extendResultsContainer.setPreferredSize(new Dimension(CARD_WIDTH, 25));

        // Button that enables the user to go to travel details
        JButton extendResultButton = new JButton("DETAILS");extendResultButton.setFont(h2);
        extendResultButton.setForeground(Color.decode(PRIMARY_COLOR));
        extendResultButton.setBackground(Color.decode("#FAFAFA"));
        extendResultButton.setBorder(BorderFactory.createLineBorder(Color.decode("#FAFAFA")));
        // TODO: add actionlistner that makes it possible to change to ExtendedResult screen

        extendResultButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(getExtendedResult(itinerary));
            }

        });

        extendResultsContainer.add(extendResultButton);
        pResultTextContainer.add(extendResultsContainer);

        busIconLabel.setIcon(getCrowdednessIcon(crowdednessIndicator));
        busIcon.add(busIconLabel);

        busIcon.setBackground(Color.decode(SECONDARY_COLOR));

        this.add(busIcon);
        this.add(pResultTextContainer);
        this.add(detailsSpacer);
        this.add(extendResultButton);


        JPanel pDivider = new JPanel();
        pDivider.setPreferredSize(new Dimension(CARD_WIDTH, 15));

        }


    // TODO: Change screen when detail button is pressed

    private JPanel getExtendedResult(Itinerary itinerary) {
        JPanel ExtendedResult = new ExtendedResult(itinerary);
        return ExtendedResult;
    }


    private void changeScreen (JPanel panelToChangeTo){
        JPanel parent = (JPanel)getParent().getParent().getParent().getParent().getParent();
        parent.removeAll();
        parent.add(getExtendedResult(itinerary));

        parent.revalidate();
        parent.repaint();
    }

    private void openMenu (){

    }


    public ImageIcon getCrowdednessIcon(CapacityCalculator.CrowdednessIndicator crowdednessIndicator){

        ImageIcon crowdednessIcon;

        if (crowdednessIndicator == CapacityCalculator.CrowdednessIndicator.RED){
            crowdednessIcon = red;

        }else if (crowdednessIndicator == CapacityCalculator.CrowdednessIndicator.ORANGE){
            crowdednessIcon = yellow;

        }else {
            crowdednessIcon = green;

        }
        return crowdednessIcon;
        }
    }




