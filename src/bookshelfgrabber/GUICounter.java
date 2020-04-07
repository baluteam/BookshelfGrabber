/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookshelfgrabber;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 */
public class GUICounter {
    private JFrame frame;
    
    public GUICounter(int countDownFromInSec, Consumer callback) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        CountDownPanel countDownPanel = new CountDownPanel(countDownFromInSec, callback, this);
        frame.add(countDownPanel);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
    
    public void dismiss() {
        frame.dispose();
    }
}

class CountDownPanel extends JPanel {
    private JLabel label;
    private Timer timer;
    private int countDownFromInSec;
    private final int SEC_IN_MILLIS = 1000;
    //private final int DELAY_IN_SEC = 1;
    private Consumer callback;
    private GUICounter callbackObj;

    public CountDownPanel(int countDownFromInSec, Consumer<GUICounter> callback, GUICounter callbackObj) {
        this();
        this.countDownFromInSec = countDownFromInSec + 1;
        this.callback = callback;
        this.callbackObj = callbackObj;
    }
    
    private CountDownPanel() {
        label = new JLabel(Integer.toString(countDownFromInSec));
        setLayout(new GridBagLayout());
        add(label);
        timer = new Timer(SEC_IN_MILLIS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countDownFromInSec--;
                if (0 < countDownFromInSec) {
                    label.setText(Integer.toString(countDownFromInSec));
                } else {
                    ((Timer) (e.getSource())).stop();
                    callbackObj.dismiss();
                    callback.accept(null);
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}
