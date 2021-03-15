package prova.swing.scroll;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class PanScroll extends JPanel implements MouseMotionListener {

  /** serialVersionUID long */
  private static final long serialVersionUID = 3859261614213669595L;

  public PanScroll() {
    initialize();
  }

  private void initialize() {
    setAutoscrolls(true);
  }

  @Override
  public void paint(Graphics p_g) {
    Graphics2D g2 = (Graphics2D) p_g;
    int p1x = 0;
    int p1y = 0;
    int p2x = 0;
    int p2y = 0;
    for (int i = 0; i < 30; i++) {
      if ( (i & 1) == 0)
        p2x = p1x + 40;
      else
        p2y = p1y + 30;
      g2.drawLine(p1x, p1y, p2x, p2y);
      p1x = p2x;
      p1y = p2y;
    }
    setPreferredSize(new Dimension(p2x, p2y));
  }

  @Override
  public void mouseDragged(MouseEvent p_e) {
    System.out.println("PanScroll.mouseDragged():" + p_e.toString());

  }

  @Override
  public void mouseMoved(MouseEvent p_e) {
    System.out.println("PanScroll.mouseMoved():" + p_e.toString());

  }

}
