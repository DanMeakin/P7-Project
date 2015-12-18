package main.gui;

import main.CapacityCalculator;
import main.DataLoader;
import main.Stop;
import main.routeplanner.ItineraryFinder;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;


/**
 * * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin 02-12-2015.
 */


public class StartScreen extends JFrame {

    // Constants
    private final int SCREEN_HEIGHT = 768;
    private final int SCREEN_WIDTH = 480;
    public static final String PRIMARY_COLOR = "#009688";
    private final String TEXT_COLOR = "#616161";
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);


    // Private members
    private JPanel startPage;
    private JPanel pPageContainer;
    private TopBar pTop;
    private  ButtonGroup busFilter;

    // Variabler til formular
    private AutoComboBox fromBox;
    private AutoComboBox destinationBox;
    private JXDatePicker datePicker;
    private JTextField timeTextField;


    private ItineraryFinder itineraryFinder;
    private DataLoader dataLoader;


    public static void main (String args[]){
        new StartScreen();

    }

    public StartScreen(){
        super ("Compute My Commute");
        setSize (new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load data
        dataLoader = new DataLoader("data/mock");

        // TopBar
        pTop = new TopBar();
        pTop.newSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(getStartPage());
            }

        });

        pTop.exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Content Container
        pPageContainer = new JPanel();
        pPageContainer.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        pPageContainer.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT - 70));

        startPage = getStartPage();
        pPageContainer.add(startPage);

        JPanel pMainContainer = new JPanel();
        pMainContainer.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        pMainContainer.add(pTop);
        pMainContainer.add(pPageContainer);
        add(pMainContainer);
        pack();
        setVisible(true);
    }

    private JPanel getStartPage() {
        // Background
        JPanel pBackground = new JPanel ();
        //pBackground.setBackground(Color.decode("#FAFAFA"));
        pBackground.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        pBackground.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));

        // Content Container
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

        // Row Panels
        // From Container
        JPanel fromContainer = new JPanel();
        fromContainer.setPreferredSize(new Dimension(430,60));

        // From Container - From: Label
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setPreferredSize(new Dimension(120,80));
        fromLabel.setFont(h2);
        fromLabel.setForeground(Color.decode(TEXT_COLOR));
        fromContainer.add(fromLabel);

        // From Container - From autocompletionbox
        // TODO: Make it possible to press enter so the dropdrown disapreares. Find a way to not show suggestions unless he user has begun typing and limit the amount of suggestions shown.
        // TODO: Set Default button!

        fromBox = new AutoComboBox();
        // TODO: Udskiftes med resultatsæt fra rejseplanen.
        List<Stop> stopList = Stop.getAllStops();
        String[] stopArray = new String[Stop.numberOfStops()];
        for (int i = 0; i< stopArray.length; i++){
            stopArray[i] = stopList.get(i).getName();
        }


        fromBox.setKeyWord(stopArray);
        fromBox.setPreferredSize(new Dimension(230,30));
        fromBox.setBackground(Color.decode("#FAFAFA"));
        fromContainer.add(fromBox);

        // Destination Container
        JPanel destinationContainer = new JPanel();
        destinationContainer.setPreferredSize(new Dimension(430,80));

        // Destination Container - Destination: Label
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setPreferredSize(new Dimension(120,80));
        destinationLabel.setFont(h2);
        destinationLabel.setForeground(Color.decode(TEXT_COLOR));
        destinationContainer.add(destinationLabel);

        // Destination Container - Destination autocompletionbox
        // TODO: Make it possible to press enter so the dropdrown disapreares. Find a way to not show suggestions unless he user has begun typing and limit the amount of suggestions shown.
        // TODO: Set Default button Show all Buses!

        destinationBox = new AutoComboBox();

        // TODO: Udskiftes med resultatsæt fra rejseplanen.
        destinationBox.setKeyWord(stopArray);
        destinationBox.setPreferredSize(new Dimension(230,30));
        destinationBox.setBackground(Color.decode("#FAFAFA"));
        destinationContainer.add(destinationBox);

        // Space Container Destination and divider
        JPanel destinationSpacer = new JPanel();
        destinationSpacer.setPreferredSize(new Dimension(430,5));

        // Date Container
        JPanel dateContainer = new JPanel();
        dateContainer.setPreferredSize(new Dimension(430,50));

        // Date Container - Date Label
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(h3);
        dateLabel.setForeground(Color.decode(TEXT_COLOR));
        dateContainer.add(dateLabel);

        // Date picker

        datePicker = new JXDatePicker();

        // edit the Button
        JButton dateBtn = (JButton)datePicker.getComponent(1);
        Image editImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/main/gui/assets/icons/calendarSmall.png"));

        dateBtn.setIcon(new ImageIcon(editImage));
        dateBtn.setFocusPainted(false);
        dateBtn.setMargin(new Insets(0, 0, 0, 0));
        dateBtn.setContentAreaFilled(false);
        dateBtn.setBorderPainted(false);
        dateBtn.setOpaque(false);

        dateContainer.add(datePicker);

        // Space Container date and Leaving
        JPanel datePickerSpacer = new JPanel();
        datePickerSpacer.setPreferredSize(new Dimension(95,60));
        datePickerSpacer.setBackground(Color.decode("#FAFAFA"));

        dateContainer.add(datePickerSpacer);

        // JRadioButton for leaving/arriving
        // TODO: Add actionlistener to all RadioButtonGruops
        JRadioButton leavingButton = new JRadioButton("Leaving", true);
        leavingButton.setBackground(Color.decode("#FAFAFA"));
        leavingButton.setFont(h3);
        leavingButton.setForeground(Color.decode(TEXT_COLOR));

        JRadioButton arrivingButton = new JRadioButton("Arriving");
        arrivingButton.setBackground(Color.decode("#FAFAFA"));
        arrivingButton.setFont(h3);
        arrivingButton.setForeground(Color.decode(TEXT_COLOR));

        ButtonGroup leavingGroup = new ButtonGroup();
        leavingGroup.add(leavingButton);

        leavingGroup.add(arrivingButton);

        dateContainer.add(leavingButton);

        // Time Container
        JPanel timeContainer = new JPanel();
        timeContainer.setPreferredSize(new Dimension(430,60));

        // Time Container - Time Label
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(h3);
        timeLabel.setForeground(Color.decode(TEXT_COLOR));
        timeContainer.add(timeLabel);

        // Time Container - time Textfield

        timeTextField = new JTextField("");
        timeTextField.setPreferredSize(new Dimension(100,15));

        timeContainer.add(timeTextField);

        // Space Container time and Arriving
        JPanel timePickerSpacer = new JPanel();
        timePickerSpacer.setPreferredSize(new Dimension(205,60));
        timePickerSpacer.setBackground(Color.decode("#FAFAFA"));

        timeContainer.add(timePickerSpacer);

        // Adds radiobutton with arriving
        timeContainer.add(arrivingButton);

        // Space between filter and search container
        JPanel filterSpacer = new JPanel();
        filterSpacer.setPreferredSize(new Dimension(340,10));
        filterSpacer.setBackground(Color.decode("#FAFAFA"));

        // Crowdedness Filter Container
        JPanel allBussesContainer = new JPanel();
        allBussesContainer.setPreferredSize(new Dimension(430,60));

        // Crowdedness Filter
        JRadioButton allBusesButton = new JRadioButton("Show All Buses", true);
        allBusesButton.setBackground(Color.decode("#FAFAFA"));
        allBusesButton.setFont(h3);
        allBusesButton.setForeground(Color.decode(TEXT_COLOR));

        JRadioButton mediumBusesButton = new JRadioButton("Show buses with available room");
        mediumBusesButton.setBackground(Color.decode("#FAFAFA"));
        mediumBusesButton.setFont(h3);
        mediumBusesButton.setForeground(Color.decode(TEXT_COLOR));

        JRadioButton freeBusesButton = new JRadioButton("Show buses with available seats");
        freeBusesButton.setBackground(Color.decode("#FAFAFA"));
        freeBusesButton.setFont(h3);
        freeBusesButton.setForeground(Color.decode(TEXT_COLOR));

        busFilter = new ButtonGroup();
        busFilter.add(allBusesButton);
        busFilter.add(mediumBusesButton);
        busFilter.add(freeBusesButton);

        allBussesContainer.add(allBusesButton);

        // Spacer after all Busses
        JPanel allBusesSpacer = new JPanel();;
        allBusesSpacer.setPreferredSize(new Dimension(235,50));
        allBusesSpacer.setBackground(Color.decode("#FAFAFA"));

        allBussesContainer.add(allBusesSpacer);

        // medium Container
        JPanel mediumContainer = new JPanel();
        mediumContainer.setPreferredSize(new Dimension(430,60));
        mediumContainer.add(mediumBusesButton);

        // Space before medium bus Icon
        JPanel mediumBusIconSpacer = new JPanel();
        mediumBusIconSpacer.setPreferredSize(new Dimension(45,50));
        mediumBusIconSpacer.setBackground(Color.decode("#FAFAFA"));

        mediumContainer.add(mediumBusIconSpacer);

        // medium Icon container
        JPanel mediumIconContainer = new JPanel();
        mediumContainer.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessMedium.png"))));
        mediumIconContainer.setPreferredSize(new Dimension(100,50));

        // Spacer after medium buses
        JPanel mediumBusesSpacer = new JPanel();
        mediumBusesSpacer.setPreferredSize(new Dimension(35,50));
        mediumBusesSpacer.setBackground(Color.decode("#FAFAFA"));

        mediumContainer.add(mediumBusesSpacer);

        // Free Container
        JPanel freeContainer = new JPanel();
        freeContainer.setPreferredSize(new Dimension(430,60));
        freeContainer.add(freeBusesButton);

        // Space before free bus Icon
        JPanel busIconSpacer = new JPanel();
        busIconSpacer.setPreferredSize(new Dimension(45,50));
        busIconSpacer.setBackground(Color.decode("#FAFAFA"));

        freeContainer.add(busIconSpacer);

        // free Icon container
        JPanel freeIconContainer = new JPanel();
        freeContainer.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessUncrowded.png"))));
        freeIconContainer.setPreferredSize(new Dimension(200,50));

        // Spacer after free buses
        JPanel freeBusesSpacer = new JPanel();
        freeBusesSpacer.setPreferredSize(new Dimension(35,50));
        freeBusesSpacer.setBackground(Color.decode("#FAFAFA"));

        freeContainer.add(freeBusesSpacer);

        // Space between filter and search container
        JPanel filterSearchSpacer = new JPanel();
        filterSearchSpacer.setPreferredSize(new Dimension(340,10));
        filterSearchSpacer.setBackground(Color.decode("#FAFAFA"));


        // Search Container
        JPanel searchContainer = new JPanel();
        searchContainer.setPreferredSize(new Dimension(430,65));

        // Search Container Space
        JPanel searchContainerSpace = new JPanel();
        searchContainerSpace.setPreferredSize(new Dimension(280,65));
        searchContainerSpace.setBackground(Color.decode("#FAFAFA"));
        searchContainer.add(searchContainerSpace);

        // Search Container - Find Bus Button
        JButton findBusButton = new JButton("FIND BUS");
        findBusButton.setFont(h2);
        findBusButton.setForeground(Color.decode(PRIMARY_COLOR));
        findBusButton.setBackground(Color.decode("#FAFAFA"));
        findBusButton.setBorder(BorderFactory.createLineBorder(Color.decode("#FAFAFA")));
        findBusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*// 1 Henter alle knapper i Bus filter gruppen

                // 2 Henter første element fra Enumeration
                JRadioButton firstElement = (JRadioButton)allBusRadioButtons.nextElement();
                // 3 Er knappen valgt
                boolean isSelected = firstElement.isSelected();*/


                // Get selected stop object (From)
                int fromBoxSelectionIndex = fromBox.getSelectedIndex();
                Stop selectedFromStop = Stop.getAllStops().get(fromBoxSelectionIndex);

                // Get selected stop object (Destination)
                int destinationBoxSelectionIndex = destinationBox.getSelectedIndex();
                Stop selectedDestinationStop = Stop.getAllStops().get(destinationBoxSelectionIndex);

                Date date = datePicker.getDate();
                // Time is chosen by the system default timezone
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                String time = timeTextField.getText().toString();
                LocalTime localTime = LocalTime.parse(time);

                LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

                itineraryFinder = new ItineraryFinder(selectedFromStop,selectedDestinationStop,localDateTime);

                // Set filter if selected
                Enumeration allBusRadioButtons = busFilter.getElements();
                JRadioButton red = (JRadioButton)allBusRadioButtons.nextElement();
                JRadioButton orange = (JRadioButton)allBusRadioButtons.nextElement();
                JRadioButton green = (JRadioButton)allBusRadioButtons.nextElement();

                if (orange.isSelected()){
                    itineraryFinder.setFilter(CapacityCalculator.crowdednessIndicator.ORANGE);
                } else if(green.isSelected()){
                    itineraryFinder.setFilter(CapacityCalculator.crowdednessIndicator.GREEN);
                }


                changeScreen(getPage2(itineraryFinder));


            }
        });
        searchContainer.add(findBusButton);

        // Last add
        /*pContentContainer.add(barSpacer);*/
        pContent.add(fromContainer);
        pContent.add(destinationContainer);
        pContent.add(destinationSpacer);
        pContent.add(getDivider());
        pContent.add(dateContainer);
        pContent.add(timeContainer);
        pContent.add(getDivider());
        pContent.add(filterSpacer);
        pContent.add(allBussesContainer);
        pContent.add(mediumContainer);
        pContent.add(freeContainer);
        pContent.add(filterSearchSpacer);
        pContent.add(getDivider());
        pContent.add(searchContainer);

        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode("#FAFAFA"));

        }

        pContentContainer.add(pContent);
        pBackground.add(pContentContainer);

        return pBackground;
    }


    private JPanel getPage2(ItineraryFinder itineraryFinder) {
        JPanel page2 = new Page2(itineraryFinder);

        return page2;
    }

    private void changeScreen (JPanel panelToChangeTo){
        pPageContainer.removeAll();
        pPageContainer.add(panelToChangeTo);

        revalidate();
        repaint();
    }

    private void openMenu (){

    }

    private JSeparator getDivider(){
        //Content divider
        JSeparator dividerContent = new JSeparator();
        dividerContent.setForeground(Color.decode("#BDBDBD"));
        dividerContent.setPreferredSize(new Dimension(430,1));
        return dividerContent;
    }


}

