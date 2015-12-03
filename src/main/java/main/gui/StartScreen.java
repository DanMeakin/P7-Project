package main.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Helle on 02-12-2015.
 */


public class StartScreen extends JFrame {
    JPanel pBackground = new JPanel ();
    JPanel pContentContainer = new JPanel();
    // Topbar
    JPanel pTop = new JPanel ();
    ImageIcon topMenuButtenIcon;
    JButton topMenuButton;

    public static void main (String args[]){
        new StartScreen();
    }

    public StartScreen(){
        super ("StartScreen");
        setSize (400,600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pContentContainer.setLayout(new GridLayout(1,7));

        add(pBackground);
        add(pContentContainer);

        // Topbar
        topMenuButtenIcon = new ImageIcon(this.getClass().getResource("/main/gui/assets/icons/menu.png"));
        //Image hamburgermenu = topMenuButtenIcon.getImage();


        //hamburgermenu = hamburgermenu.getScaledInstance(50,50,Image.SCALE_SMOOTH);
        //topMenuButtenIcon.setImage(hamburgermenu);
        topMenuButton = new JButton(topMenuButtenIcon);
        topMenuButton.setPreferredSize(new Dimension(50,50));
        topMenuButton.setBackground(Color.decode("#B2DFDB"));
        //topMenuButton.setContentAreaFilled(false);
        topMenuButton.setBorder(BorderFactory.createLineBorder(Color.decode("#B2DFDB")));
        topMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Skriv kode til f.eks. menu her
                System.out.println ("Re");
                // Remove top bar panel from JFrame
                // (skift fra en sde til en anden)
                remove(pTop);
                //TODO: tiføj et panel der udgør en anden side
                JPanel page2 = getPage2();
                add(page2);

                // Update rendered JFrame
                repaint();
            }
        });



        pTop.setBackground(Color.decode("#B2DFDB"));

        pTop.add(topMenuButton);

        add(pTop);


        setVisible(true);
    }

    private JPanel getPage2() {
        JPanel page2 = new JPanel();
        //TODO: Create comonents from page 2 and add to JPanel

        return page2;
    }

}


