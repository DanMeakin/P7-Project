package main.gui;
import javafx.scene.control.RadioButton;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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

    public static void main (String args[]){
        new StartScreen();

    }

    public StartScreen(){
        super ("StartScreen");
        setSize (new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        startPage = getStartPage();
        add(startPage);

        pack();
        setVisible(true);
    }

    private JPanel getStartPage() {
        // Background
        JPanel pBackground = new JPanel ();
        pBackground.setBackground(Color.decode("#B2DFDB"));
        pBackground.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        pBackground.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));

        // Topbar
        JPanel pTop = new JPanel ();
        pTop.setPreferredSize(new Dimension(SCREEN_WIDTH,70));
        pTop.setBackground(Color.decode(PRIMARY_COLOR));
        pTop.setLayout(new FlowLayout(FlowLayout.LEADING,0,10));

        // Topbar - JMenuBar
        JMenuBar topMenuBar = new JMenuBar();

        // JMenubar - Menu/Button
        JMenu topMenu = new JMenu("");
        ImageIcon topMenuButtonIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        topMenu.setIcon(topMenuButtonIcon);
        topMenu.setOpaque(true);
        topMenu.setBackground(Color.decode(PRIMARY_COLOR));
        topMenu.setBorder(BorderFactory.createLineBorder(Color.decode(PRIMARY_COLOR),6));

        JMenuItem newSearch = new JMenuItem("New Search");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem about = new JMenuItem("About");
        JMenuItem exit = new JMenuItem("Exit");

        newSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(getStartPage());
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        topMenu.add(newSearch);
        topMenu.add(settings);
        topMenu.add(about);
        topMenu.add(exit);
        topMenuBar.add(topMenu);
        pTop.add(topMenuBar);

        // Topbar - App Name Label
        JLabel appNameLabel = new JLabel("Compute My Commute");
        appNameLabel.setFont(h1);
        appNameLabel.setForeground(Color.white);

        // Content Container
        JPanel pContentContainer = new JPanel();
        pContentContainer.setLayout(new CardLayout(15,15));

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
        pContentContainer.add(pContent);

        // Row Panels
        // Space panel
        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(430,5));

        // From Container
        JPanel fromContainer = new JPanel();
        fromContainer.setPreferredSize(new Dimension(430,80));

        // From Container - From: Label
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setPreferredSize(new Dimension(120,80));
        fromLabel.setFont(h2);
        fromLabel.setForeground(Color.decode(TEXT_COLOR));
        fromContainer.add(fromLabel);

        // From Container - From autocompletionbox
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
        AutoComboBox destinationBox = new AutoComboBox();
        //TODO: Udskiftes med resultatsæt fra rejseplanen.
       //String[] itemArray = {"Sejrøgade","Bornholmsgade", "Nytorv"};
        destinationBox.setKeyWord(itemArray);
        destinationBox.setPreferredSize(new Dimension(230,30));
        destinationBox.setBackground(Color.decode("#FAFAFA"));
        destinationContainer.add(destinationBox);

        // Date Container
        JPanel dateContainer = new JPanel();
        dateContainer.setPreferredSize(new Dimension(430,75));

        // Date Container - Date Label
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(h3);
        dateLabel.setForeground(Color.decode(TEXT_COLOR));
        dateContainer.add(dateLabel);

        // Date Container - Date Textfield
        JTextField dateTextField = new JTextField("02/12/15");
        dateContainer.add(dateTextField);

        // TODO: Make Icon for date picker and make date picker(Spinner)

        // JRadioButton for leaving/arriving
        JRadioButton leavingButton = new JRadioButton("Leaving");
        JRadioButton arrivingButton = new JRadioButton("Arriving");

        ButtonGroup leavingGroup = new ButtonGroup();
        leavingGroup.add(leavingButton);
        leavingGroup.add(arrivingButton);

        dateContainer.add(leavingButton);

        // TODO: Add Checkbox and label for Leaving

        // Time Container
        JPanel timeContainer = new JPanel();
        timeContainer.setPreferredSize(new Dimension(430,75));

        // Time Container - Time Label
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(h3);
        timeLabel.setForeground(Color.decode(TEXT_COLOR));
        timeContainer.add(timeLabel);

       // Time Container - time Textfield
        JTextField timeTextField = new JTextField("  :  :  ");
        timeContainer.add(timeTextField);

        // TODO: Make Icon for time picker and make time picker(Spinner)

        // TODO: Add CheckBox and label For Arriving
        timeContainer.add(arrivingButton);

        // Crowdedness Filter Container
        JPanel allBussesContainer = new JPanel();
        allBussesContainer.setPreferredSize(new Dimension(430,70));

        // Crowdedness Filter Container
        JRadioButton allBussesButton = new JRadioButton("Show All Busses");
        JRadioButton mediumBusesButton = new JRadioButton("Show only buses with free standing space");
        JRadioButton freeBusesButton = new JRadioButton("Show only buses with free standing space and seating");

        ButtonGroup busFilter = new ButtonGroup();
        busFilter.add(allBussesButton);
        busFilter.add(mediumBusesButton);
        busFilter.add(freeBusesButton);

        allBussesContainer.add(allBussesButton);

        // Crowdedness Filter Container
        JPanel mediumContainer = new JPanel();
        mediumContainer.setPreferredSize(new Dimension(430,70));

        mediumContainer.add(mediumBusesButton);

        // Crowdedness Filter Container
        JPanel freeContainer = new JPanel();
        freeContainer.setPreferredSize(new Dimension(430,70));

        freeContainer.add(freeBusesButton);

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
        pContent.add(spacePanel);
        pContent.add(fromContainer);
        pContent.add(destinationContainer);
        pContent.add(getDivider());
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

        pTop.add(appNameLabel);
        pBackground.add(pTop);
        pBackground.add(pContentContainer);

        return pBackground;
    }


    private JPanel getPage2() {
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
    }

    private void changeScreen (JPanel panelToChangeTo){
        getContentPane().removeAll();
        getContentPane().add(panelToChangeTo);
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

