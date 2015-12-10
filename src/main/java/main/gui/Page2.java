package main.gui;

import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.javafx.scene.control.skin.ScrollBarSkin;
import org.jdesktop.swingx.border.DropShadowBorder;
import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
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
    private final Font h1 = new Font("Roboto", Font.PLAIN, 24);
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);

    public Page2(){
        //Background
        super();
        //this.setBackground(Color.decode(SECONDARY_COLOR));
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        //Search Container
        JPanel pSearchContainer = new JPanel();
        pSearchContainer.setPreferredSize(new Dimension(SCREEN_WIDTH, 50));
        //pSearchContainer.setBackground(Color.decode(SECONDARY_COLOR));
        //pSearchContainer.setLayout(new CardLayout());

        //Search Content
        JPanel pSearchContent = new JPanel();
        pSearchContent.setLayout(new GridLayout(1, 3));

        pSearchContent.setPreferredSize(new Dimension(SCREEN_WIDTH -30, SCREEN_HEIGHT -720));
        //pSearchContent.setBackground(Color.decode(SECONDARY_COLOR));

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

        //Search Container - From Textfield
        JLabel fromLabel = new JLabel ("From");
        pSearchContent.add(fromLabel);
        fromLabel.setFont(h2);

        //Search Container - Icon
        JPanel pSearchIcon = new JPanel();
        pSearchIcon.setLayout(new GridLayout(0,1));
        pSearchIcon.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/arrow.png"))));
        pSearchIcon.setPreferredSize(new Dimension(48, 48));
        pSearchIcon.setBackground(Color.decode(SECONDARY_COLOR));
        pSearchContent.add(pSearchIcon);

        //Search Container - To Textfield
        JLabel toLabel = new JLabel ("To");
        pSearchContent.add(toLabel);
        toLabel.setFont(h2);

        //Scrollbar
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUI(newScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5,getHeight()));
        scrollPane.getVerticalScrollBar().setUnitIncrement(24);

        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setPreferredSize(new Dimension(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 130));

        // Content
        JPanel pContent = new JPanel();
        //pContent.setBackground(Color.decode("#783478"));
        // ønskes fleksibel layout for resultater find Layoutmanager og set layout til automatisk at tilpasse indhold(GridBagLayout)
        pContent.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + 1700));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode(SECONDARY_COLOR), 0));

        for (int i = 0; i < pContent.getComponents().length; i++) {
            pContent.getComponent(i).setBackground(Color.decode(SECONDARY_COLOR));

        }

        // All Results Container
        //JPanel allResultsContainer =

        //results
            //result 0
        JPanel pResult = new JPanel();
        pResult.setLayout(new FlowLayout(FlowLayout.LEFT));
        pResult.setBackground(Color.decode(SECONDARY_COLOR));
        pResult.setPreferredSize((new Dimension(SCREEN_WIDTH -50, 150)));

        JPanel busIcon = new JPanel();
        busIcon.setLayout(new GridLayout(0, 1));
        busIcon.setPreferredSize(new Dimension (100, 100));
        busIcon.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessCrowded-100x100.png"))));
        pResult.add(busIcon);
        busIcon.setBackground(Color.decode(SECONDARY_COLOR));

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
        pResult.add(pResultTextContainer);

            //result1
        JPanel pResult1 = new JPanel();
        pResult1.setLayout(new FlowLayout(FlowLayout.LEFT));
        pResult1.setBackground(Color.decode(SECONDARY_COLOR));
        pResult1.setPreferredSize((new Dimension(SCREEN_WIDTH -50, 150)));

        JPanel busIcon1 = new JPanel();
        busIcon1.setLayout(new GridLayout(0, 1));
        busIcon1.setPreferredSize(new Dimension (100, 100));
        busIcon1.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessNotCrowded.png"))));
        pResult1.add(busIcon1);
        busIcon1.setBackground(Color.decode(SECONDARY_COLOR));

        JPanel pResultTextContainer1 = new JPanel();
        pResultTextContainer1.setLayout(new GridLayout(2, 2));

        JLabel pResultNumber1 = new JLabel("2B                Monday, 30.11.2015");
        pResultNumber1.setFont(h2);
        pResultNumber1.setForeground(Color.BLACK);
        pResultTextContainer1.add(pResultNumber1);


        JLabel pResultInfo1 = new JLabel("Depature 15:25, BornHolmsgade (Aalborg), 45 min.");
        pResultInfo1.setFont(h3);
        pResultInfo1.setForeground(Color.BLACK);
        pResultTextContainer1.setBackground(Color.decode(SECONDARY_COLOR));
        pResultTextContainer1.setPreferredSize(new Dimension(300, 150));
        pResultTextContainer1.add(pResultInfo1);
        pResult1.add(pResultTextContainer1);

            //result 2
        JPanel pResult2 = new JPanel();
        pResult2.setLayout(new FlowLayout(FlowLayout.LEFT));
        pResult2.setBackground(Color.decode(SECONDARY_COLOR));
        pResult2.setPreferredSize((new Dimension(SCREEN_WIDTH -50, 150)));

        JPanel busIcon2 = new JPanel();
        busIcon2.setLayout(new GridLayout(0, 1));
        busIcon2.setPreferredSize(new Dimension (100, 100));
        busIcon2.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessMediumCrowded.png"))));
        pResult2.add(busIcon2);
        busIcon2.setBackground(Color.decode(SECONDARY_COLOR));

        JPanel pResultTextContainer2 = new JPanel();
        pResultTextContainer2.setLayout(new GridLayout(2, 2));

        JLabel pResultNumber2 = new JLabel("2A                Monday, 30.11.2015");
        pResultNumber2.setFont(h2);
        pResultNumber2.setForeground(Color.BLACK);
        pResultTextContainer2.add(pResultNumber2);


        JLabel pResultInfo2 = new JLabel("Depature 15:36, BornHolmsgade (Aalborg), 45 min.");
        pResultInfo2.setFont(h3);
        pResultInfo2.setForeground(Color.BLACK);
        pResultTextContainer2.setBackground(Color.decode(SECONDARY_COLOR));
        pResultTextContainer2.setPreferredSize(new Dimension(300, 150));
        pResultTextContainer2.add(pResultInfo2);
        pResult2.add(pResultTextContainer2);

            //result 3
        JPanel pResult3 = new JPanel();
        pResult3.setLayout(new FlowLayout(FlowLayout.LEFT));
        pResult3.setBackground(Color.decode(SECONDARY_COLOR));
        pResult3.setPreferredSize((new Dimension(SCREEN_WIDTH -50, 150)));

        JPanel busIcon3 = new JPanel();
        busIcon3.setLayout(new GridLayout(0, 1));
        busIcon3.setPreferredSize(new Dimension (100, 100));
        busIcon3.add(new JLabel(new ImageIcon(getClass().getResource("/main/gui/assets/icons/crowdednessNotCrowded.png"))));
        pResult3.add(busIcon3);
        busIcon3.setBackground(Color.decode(SECONDARY_COLOR));

        JPanel pResultTextContainer3 = new JPanel();
        pResultTextContainer3.setLayout(new GridLayout(2, 2));

        JLabel pResultNumber3 = new JLabel("2E                Monday, 30.11.2015");
        pResultNumber3.setFont(h2);
        pResultNumber3.setForeground(Color.BLACK);
        pResultTextContainer3.add(pResultNumber3);


        JLabel pResultInfo3 = new JLabel("Depature 15:54, BornHolmsgade (Aalborg), 54 min.");
        pResultInfo3.setFont(h3);
        pResultInfo3.setForeground(Color.BLACK);
        pResultTextContainer3.setBackground(Color.decode(SECONDARY_COLOR));
        pResultTextContainer3.setPreferredSize(new Dimension(300, 150));
        pResultTextContainer3.add(pResultInfo3);
        pResult3.add(pResultTextContainer3);

        //Divider
        JPanel pDivider = new JPanel();
        pDivider.setPreferredSize(new Dimension(SCREEN_WIDTH, 15));

        JPanel pDivider1 = new JPanel();
        pDivider1.setPreferredSize(new Dimension(SCREEN_WIDTH, 15));

        JPanel pDivider2 = new JPanel();
        pDivider2.setPreferredSize(new Dimension(SCREEN_WIDTH, 15));

        //Last adds
        scrollPane.setViewportView(pContent);
        this.add(pSearchContainer);
        this.add(scrollPane);

        pContent.add(pResult);
        pContent.add(pDivider);
        pContent.add(pResult1);
        pContent.add(pDivider1);
        pContent.add(pResult2);
        pContent.add(pDivider2);
        pContent.add(pResult3);

        pContent.add(new ResultCard());

        setVisible(true);
    }

    /**
     * @return The ScrollBarUI to use
     */
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



