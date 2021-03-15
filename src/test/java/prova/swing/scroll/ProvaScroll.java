package prova.swing.scroll;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class ProvaScroll extends JFrame {

  /** serialVersionUID long */
  private static final long serialVersionUID = -7202914775847935434L;
  private JPanel            m_pan;

  public ProvaScroll() {
    inizializza();
  }

  public static void main(String[] args) {
    new PropertyChangeBroadcaster();
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        ProvaScroll fram = new ProvaScroll();
        fram.setVisible(true);
      }
    });
  }

  private void inizializza() {
    setSize(400, 600);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    m_pan = new PanScroll();
    m_pan.setPreferredSize(new Dimension(400, 300));
    JScrollPane scrollPane = new JScrollPane(m_pan, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    getContentPane().add(scrollPane, BorderLayout.CENTER);

    // scrollPane.setViewportView(m_pan);

    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.NORTH);

    JLabel lblNewLabel = new JLabel("Prima");
    panel.add(lblNewLabel);

    NumberFormat intForm = NumberFormat.getNumberInstance();
    intForm.setMaximumFractionDigits(2);
    JFormattedTextField txPrimo = new JFormattedTextField();
    txPrimo.setColumns(10);
    panel.add(txPrimo);

  }

}
