package main.gui;

import com.sun.javafx.scene.control.skin.ScrollBarSkin;
import main.CapacityCalculator;
import main.routeplanner.Itinerary;
import main.routeplanner.ItineraryFinder;
import main.routeplanner.ItineraryLeg;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.print.DocFlavor;
import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
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
    private final String TEXT_COLOR = "#616161";
    private final String FIRST_TEXT_COLOR = "#212121";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);


    private ItineraryFinder itineraryFinder;


    public Page2(ItineraryFinder itineraryFinder){

        // Background
        super();
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));


        this.itineraryFinder = itineraryFinder;

        // Search Container
        JPanel pSearchContainer = new JPanel();
        pSearchContainer.setPreferredSize(new Dimension(SCREEN_WIDTH, 100));

        // Search Content
        JPanel pSearchContent = new JPanel();
        pSearchContent.setPreferredSize(new Dimension(SCREEN_WIDTH -30, SCREEN_HEIGHT -600));
        DropShadowBorder searchShadow = new DropShadowBorder();
        searchShadow.setShadowSize(5);
        searchShadow.setShadowColor(Color.BLACK);
        searchShadow.setShowLeftShadow(true);
        searchShadow.setShowRightShadow(true);
        searchShadow.setShowBottomShadow(true);
        searchShadow.setShowTopShadow(true);
        pSearchContent.setBorder(searchShadow);

        pSearchContainer.add(pSearchContent);

        for (int i = 0; i < pSearchContainer.getComponents().length; i++) {
            pSearchContainer.getComponent(i).setBackground(Color.decode(SECONDARY_COLOR));
        }

        // Search Container - From Label
        JLabel fromLabel = new JLabel ("From");
        fromLabel.setFont(h2);
        fromLabel.setForeground(Color.decode(TEXT_COLOR));

        pSearchContent.add(fromLabel);

        // JLabel which shows the typed From

        JLabel typeFrom = new JLabel(itineraryFinder.getStartingStop().getName());

        typeFrom.setFont(h2);
        typeFrom.setForeground(Color.decode(FIRST_TEXT_COLOR));
        pSearchContent.add(typeFrom);

        // Search Container - Arrow Icon
        JPanel pSearchIcon = new JPanel();
        pSearchIcon.setLayout(new GridLayout(0,1));
        pSearchIcon.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/arrow.png"))));
        pSearchIcon.setPreferredSize(new Dimension(48, 48));
        pSearchIcon.setBackground(Color.decode(SECONDARY_COLOR));

        pSearchContent.add(pSearchIcon);

        // Search Container - To Label
        JLabel toLabel = new JLabel ("To");
        toLabel.setForeground(Color.decode(TEXT_COLOR));
        toLabel.setFont(h2);

        pSearchContent.add(toLabel);

        // JLabel which shows the typed Destination
        JLabel typeTo = new JLabel(itineraryFinder.getEndingStop().getName());

        typeTo.setFont(h2);
        typeTo.setForeground(Color.decode(FIRST_TEXT_COLOR));
        pSearchContent.add(typeTo);



        // Scrollbar
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUI(newScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5,getHeight()));
        scrollPane.getVerticalScrollBar().setUnitIncrement(24);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setPreferredSize(new Dimension(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 130));

        // Container that contain the scrollPane, which contains the result cards
        JPanel pContent = new JPanel();
        pContent.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode(SECONDARY_COLOR), 0));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode(SECONDARY_COLOR), 0));

        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode(SECONDARY_COLOR));

        }

        // Last adds
        scrollPane.setViewportView(pContent);
        this.add(pSearchContainer);
        this.add(scrollPane);

        // loop for generation of cards. This is expected to get the best Itineraries given a start stop, destination stop and local date time (Filter)(I get a list, but sometimes i get nothing out of the list. And what exactly is n(4) equal to( Everytime i set n to more than one, i get nothing back, why?)? )
        List<Itinerary> itineraryList = itineraryFinder.findBestItineraries(4);
        for (int i = 0; i < itineraryList.size(); i++) {
            Itinerary currentItinerary = itineraryList.get(i);
            List<ItineraryLeg> itineraryLegs = currentItinerary.getLegs();

            String startTime = getTimeString(itineraryLegs.get(0).getStartTime());
            String startStop = "";
            for (ItineraryLeg leg: itineraryLegs){
                if (leg.isBus()){
                    startStop = leg.getOrigin().getName();
                    break;

                }
            }

            // Empty string is bus name - 0 equals duration time
            pContent.add(new ResultCard(currentItinerary.determineCrowdedness(),itineraryLegs.get(0).getRouteTimetable().getRoute().getNumber(),currentItinerary.getDate().toString(),startTime,startStop,0,currentItinerary));
            System.out.println(i);
        }


        setVisible(true);
    }

    private String getTimeString(int minutesFromMidnight){

        String hours = Integer.toString(minutesFromMidnight / 60);
        hours = addZero(hours);


        String minutes = Integer.toString(minutesFromMidnight % 60);
        minutes = addZero(minutes);

        return hours + ":" + minutes;

    }

    private String addZero(String timeValue){
        String resultTimeValue = timeValue;
        if (Integer.valueOf(timeValue) < 10){
            resultTimeValue = "0" + resultTimeValue;


        }
        return resultTimeValue;
    }

    /**
     * @return The ScrollBarUI to use
     */
    // Enables editing of the ScrollBar
    private static ScrollBarUI newScrollBarUI() {

        return new BasicScrollBarUI() {


            public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                super.paintThumb(g, c, thumbBounds);
                int tw = thumbBounds.width;
                int th = thumbBounds.height;

                g.translate(thumbBounds.x, thumbBounds.y);

                Graphics2D g2 = (Graphics2D) g;
                Paint gp = null;
                if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                    gp = Color.decode("#009688");
                }
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, tw - 1, th - 1, 5, 5);

                g2.drawRoundRect(0, 0, tw - 1, th - 1, 5, 5);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();

                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }


        };

    }
}



