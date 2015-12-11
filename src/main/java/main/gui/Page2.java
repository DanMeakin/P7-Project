package main.gui;

import org.jdesktop.swingx.border.DropShadowBorder;
import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;


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

        //Last adds
        scrollPane.setViewportView(pContent);
        this.add(pSearchContainer);
        this.add(scrollPane);

        pContent.add(new ResultCard(10));
        pContent.add(new ResultCard(35));
        pContent.add(new ResultCard(80));
        pContent.add(new ResultCard(30));
        pContent.add(new ResultCard(10));
        pContent.add(new ResultCard(80));
        pContent.add(new ResultCard(60));

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



