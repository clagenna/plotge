package prova.swing.jframe;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class ProvaSwingBase extends JFrame {

  /** serialVersionUID long */
  private static final long serialVersionUID = -7202914775847935434L;
  private JPanel            m_pan;

  public ProvaSwingBase() {
    inizializza();
  }

  public static void main(String[] args) {
    new PropertyChangeBroadcaster();
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        ProvaSwingBase fram = new ProvaSwingBase();
        fram.setVisible(true);
      }
    });
  }

  private void inizializza() {
    setSize(400, 600);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    m_pan = new PanProva(new BorderLayout());

    add(m_pan);

  }

}
