package main.gui;

import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;

/**
 * Created by janusalarsen on 10/12/2015.
 */
public class ResultCard extends JPanel {
    private String name;
    private final int CARD_HEIGHT = 150;
    private final int CARD_WIDTH = 430;
    private final String SECONDARY_COLOR = "#FAFAFA";
    private final Font h2 = new Font("Roboto", Font.PLAIN, 18);
    private final Font h3 = new Font("Roboto", Font.PLAIN, 14);

    public ResultCard(String name) {
        super();
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        this.setBackground(Color.decode(SECONDARY_COLOR));
        setVisible(true);

        JPanel pDivider = new JPanel();
        pDivider.setPreferredSize(new Dimension(CARD_WIDTH, 15));
        }

        private JPanel getCrowdedness(){
            JPanel pCardContainer = new JPanel();
            pCardContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
            pCardContainer.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            pCardContainer.setBackground(Color.decode(SECONDARY_COLOR));

            if(String="unCrowded") {
                return ResultCard(unCrowded);
            }else if (String = mediumCrowded) {
                return ResultCard(mediumCrowded);
            }
            }else if (String = "crowded"){
                return ResultCard(crowded);
            }

            return pCardContainer;
        }
}
