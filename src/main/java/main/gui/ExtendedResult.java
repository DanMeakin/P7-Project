package main.gui;

import main.capacitytracker.CapacityCalculator;
import main.routeplanner.Itinerary;
import main.routeplanner.ItineraryLeg;
import org.jdesktop.swingx.border.DropShadowBorder;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin 02-12-2015.
 */

public class ExtendedResult extends JPanel{

    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;

    private final String PRIMARY_COLOR = "#009688";
    private final String SECONDARY_COLOR = "#FAFAFA";
    private final String TEXT_COLOR = "#616161";
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);

    private ImageIcon green = new ImageIcon(getClass().getResource("/main/gui/assets/icons/small_green.png"));
    private ImageIcon yellow = new ImageIcon(getClass().getResource("/main/gui/assets/icons/small_yellow.png"));
    private ImageIcon red = new ImageIcon(getClass().getResource("/main/gui/assets/icons/small_red.png"));

    public ExtendedResult(Itinerary itinerary){
         super();
        // this.setBackground(Color.decode(SECONDARY_COLOR));
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        JPanel pContentContainer = new JPanel();
        // pContentContainer.setPreferredSize();
        pContentContainer.setLayout(new CardLayout(15,0)); // sets hGap and vGap to 15 each

        // Content
        JPanel pContent = new JPanel();
        pContent.setLayout(new FlowLayout(FlowLayout.LEADING));
        pContent.setBackground(Color.decode("#FAFAFA"));
        pContent.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        pContent.setPreferredSize(new Dimension(SCREEN_WIDTH - 30, SCREEN_HEIGHT - 150));
        DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowSize(5);
        shadow.setShadowColor(Color.BLACK);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);
        pContent.setBorder(shadow);

        // Travel Label container
        JPanel travelLabelContainer = new JPanel();
        travelLabelContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        travelLabelContainer.setBackground(Color.decode(SECONDARY_COLOR));

        pContent.add(travelLabelContainer);

        // Travel Label
        JLabel travelDetails = new JLabel("Travel Details");
        travelDetails.setForeground(Color.decode(TEXT_COLOR));
        travelDetails.setFont(h2);

        travelLabelContainer.add(travelDetails);

        pContent.add(getDivider());

        List<ItineraryLeg> itineraryLegsList = itinerary.getLegs();
        for(ItineraryLeg itineraryLeg : itineraryLegsList){
            JPanel legPanel = new JPanel();
            legPanel.setPreferredSize(new Dimension(430,30));
            legPanel.setBackground(Color.decode(SECONDARY_COLOR));

            // Start time Label
            JLabel startTimeLabel = new JLabel("Depature: " + Page2.getTimeString(itineraryLeg.getStartTime()), SwingConstants.LEFT);

            JLabel legLabel;

            if (itineraryLeg.isBus()) {

                // Bus icon Container
                JPanel busIcon = new JPanel();
                busIcon.setBackground(Color.decode(SECONDARY_COLOR));
                busIcon.setLayout(new GridLayout(0, 1));
                busIcon.setPreferredSize(new Dimension (60, 60));

                // Bus Icon Label
                JLabel busIconLabel = new JLabel();
                busIcon.setBackground(Color.decode(SECONDARY_COLOR));
                busIconLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
                busIconLabel.setIcon(getCrowdednessIcon(itineraryLeg.crowdedness()));

                busIcon.add(busIconLabel);

                // Leglabel container
                JPanel legLabelContainer = new JPanel();
                legLabelContainer.setPreferredSize(new Dimension(50,35));
                legLabelContainer.setBackground(Color.decode(SECONDARY_COLOR));


                // Leglab shows bus
                legLabel = new JLabel("Bus", SwingConstants.LEFT);
                legLabel.setFont(h2);
                legLabel.setBackground(Color.decode(SECONDARY_COLOR));

                legLabelContainer.add(legLabel);

                // Space from legLabel
                JPanel legLabelSpacer = new JPanel();
                legLabelSpacer.setPreferredSize(new Dimension(20,28));
                legLabelSpacer.setBackground(Color.decode(PRIMARY_COLOR));

                legLabelContainer.add(legLabelSpacer);

                // Contains bus number label
                JPanel busNumberLabelContainer = new JPanel();
                busNumberLabelContainer.setPreferredSize(new Dimension(300,60));

                busNumberLabelContainer.setBackground(Color.decode(SECONDARY_COLOR));


                // Bus number Label
                JLabel busNumberLabel = new JLabel(itineraryLeg.getRouteTimetable().getRoute().getNumber());
                busNumberLabel.setForeground(Color.decode(TEXT_COLOR));
                busNumberLabelContainer.setFont(h2);

                busNumberLabelContainer.add(busNumberLabel);

                // Contains from stop label
                JPanel firstStopLabelContainer = new JPanel();
                firstStopLabelContainer.setPreferredSize(new Dimension(275,30));
                firstStopLabelContainer.setFont(h2);
                firstStopLabelContainer.setBackground(Color.decode(SECONDARY_COLOR));

                // From stop label
                JLabel firstStopLabel = new JLabel(itineraryLeg.getOrigin().getName());

                firstStopLabelContainer.add(firstStopLabel);

                // Contains destination label
                JPanel destinationLabelContainer = new JPanel();
                destinationLabelContainer.setPreferredSize(new Dimension(275,30));
                destinationLabelContainer.setFont(h2);
                destinationLabelContainer.setBackground(Color.decode(SECONDARY_COLOR));

                // End time label
                JLabel endTime = new JLabel("Arrival: " + Page2.getTimeString(itineraryLeg.getEndTime()), SwingConstants.LEFT);

                // Destination Label
                JLabel destinationLabel = new JLabel(itineraryLeg.getDestination().getName(), SwingConstants.LEFT);

                destinationLabelContainer.add(destinationLabel);

                // Last adds in bus leg
                pContent.add(legPanel);
                pContent.add(busIcon);
                legPanel.add(legLabelContainer);
                pContent.add(busNumberLabelContainer);
                pContent.add(startTimeLabel);
                pContent.add(firstStopLabelContainer);
                pContent.add(endTime);
                pContent.add(destinationLabelContainer);

            } else if (itineraryLeg.isWalk()){

                // Leg label for walk
                legLabel = new JLabel("Walk", SwingConstants.LEFT);
                legLabel.setFont(h2);
                legLabel.setBackground(Color.decode(SECONDARY_COLOR));

                JLabel walkTime = new JLabel("" + itineraryLeg.getWalk().walkingTime());

                JLabel endTime = new JLabel(Page2.getTimeString(itineraryLeg.getEndTime()));

                pContent.add(startTimeLabel);
                pContent.add(walkTime);
                pContent.add(endTime);

            } else {

                legLabel = new JLabel("");

                pContent.add(legLabel);
            }

        }

        pContentContainer.add(pContent);
        this.add(pContentContainer);

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


    private JSeparator getDivider(){
        //Content divider
        JSeparator dividerContent = new JSeparator();
        dividerContent.setForeground(Color.decode("#BDBDBD"));
        dividerContent.setPreferredSize(new Dimension(430,1));
        return dividerContent;
    }


}
