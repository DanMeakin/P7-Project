package main.gui;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Helle on 02-12-2015.
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

    public static void main (String args[]){
        new StartScreen();

    }

    public StartScreen(){
        super ("StartScreen");
        setSize (new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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
        pBackground.setBackground(Color.decode("#FAFAFA"));
        pBackground.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        pBackground.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));

        // Content Container
        JPanel pContentContainer = new JPanel();
        // pContentContainer.setPreferredSize();
        pContentContainer.setLayout(new CardLayout(15,0)); // sets hGap and vGap to 15 each

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
        //TODO: Make it possible to press enter so the dropdrown disapreares. Find a way to not show suggestions unless he user has begun typing and limit the amount of suggestions shown.
        // TODO: Set Default button!
        AutoComboBox fromBox = new AutoComboBox();
        //TODO: Udskiftes med resultatsæt fra rejseplanen.
        String[] itemArray = {"Boulevarden","Bornholmsgade", "Nytorv"};
        fromBox.setKeyWord(itemArray);
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
        AutoComboBox destinationBox = new AutoComboBox();
        // TODO: Udskiftes med resultatsæt fra rejseplanen.
        // String[] itemArray = {"Sejrøgade","Bornholmsgade", "Nytorv"};
        destinationBox.setKeyWord(itemArray);
        destinationBox.setPreferredSize(new Dimension(230,30));
        destinationBox.setBackground(Color.decode("#FAFAFA"));
        destinationContainer.add(destinationBox);

        // Space Container Destination and divider
        JPanel destinationSpacer = new JPanel();
        destinationSpacer.setPreferredSize(new Dimension(430,5));

        // Space Container divider and date container
        JPanel dateSpacer = new JPanel();
        dateSpacer.setPreferredSize(new Dimension(430,15));

        // Date Container
        JPanel dateContainer = new JPanel();
        dateContainer.setPreferredSize(new Dimension(430,60));

        // Date Container - Date Label
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(h3);
        dateLabel.setForeground(Color.decode(TEXT_COLOR));
        dateContainer.add(dateLabel);

        // Date picker
        JXDatePicker datePicker = new JXDatePicker();

        dateContainer.add(datePicker);

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
        JTextField timeTextField = new JTextField("  :  ");
        timeContainer.add(timeTextField);

        // Adds radiobutton with arriving
        timeContainer.add(arrivingButton);

        // Crowdedness Filter Container
        JPanel allBussesContainer = new JPanel();
        allBussesContainer.setPreferredSize(new Dimension(430,70));

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

        ButtonGroup busFilter = new ButtonGroup();
        busFilter.add(allBusesButton);
        busFilter.add(mediumBusesButton);
        busFilter.add(freeBusesButton);

        allBussesContainer.add(allBusesButton);

        // medium Container
        JPanel mediumContainer = new JPanel();
        mediumContainer.setPreferredSize(new Dimension(430,70));
        mediumContainer.add(mediumBusesButton);

        // medium Icon container
        JPanel mediumIconContainer = new JPanel();
        mediumContainer.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessMedium.png"))));
        mediumIconContainer.setPreferredSize(new Dimension(100,50));

        // Free Container
        JPanel freeContainer = new JPanel();
        freeContainer.setPreferredSize(new Dimension(430,70));
        freeContainer.add(freeBusesButton);

        // free Icon container
        JPanel freeIconContainer = new JPanel();
        freeContainer.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessUncrowded.png"))));
        freeIconContainer.setPreferredSize(new Dimension(100,50));

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
                // Teksten nedenunder fjerner alt indhold og tilføjer nyt
                changeScreen(getPage2());
            }
        });
        searchContainer.add(findBusButton);

        // Last add
        pContent.add(fromContainer);
        pContent.add(destinationContainer);
        pContent.add(destinationSpacer);
        pContent.add(getDivider());
        pContent.add(dateSpacer);
        pContent.add(dateContainer);
        pContent.add(timeContainer);
        pContent.add(getDivider());
        pContent.add(allBussesContainer);
        pContent.add(mediumContainer);
        pContent.add(freeContainer);
        pContent.add(getDivider());
        pContent.add(searchContainer);

        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode("#FAFAFA"));

        }

        pContentContainer.add(pContent);
        pBackground.add(pContentContainer);

        return pBackground;
    }

    private JPanel getPage2() {
        JPanel page2 = new Page2();
        return page2;
    }

    /*private JPanel getPage2() {
        JPanel page2 = new JPanel();
        JButton newSearch = new JButton("New Search");

        //Results page background panel
        //TODO: Create components from page 2 and add to JPanel
        page2.setBackground(Color.white);
        page2.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        //New search button
        newSearch.setPreferredSize(new Dimension(200,50));
        newSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(getStartPage());
            }
        });

        page2.add(newSearch);

        return page2;
    }*/

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

