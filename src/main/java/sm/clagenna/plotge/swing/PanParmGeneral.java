package sm.clagenna.plotge.swing;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class PanParmGeneral extends JPanel implements PropertyChangeListener {

  private static final Logger       s_log            = LogManager.getLogger(PanParmGeneral.class);
  /** */
  private static final long         serialVersionUID = 877582887171358453L;
  private JTextField                m_txId;
  private JCheckBox                 m_ckStartPoint;
  private JCheckBox                 m_ckEndPoint;
  // private PlotVertice                           m_ver;
  //   @SuppressWarnings("unused") private PlotBordo m_bordo;
  private JButton                   m_btSalvaVert;
  private PropertyChangeBroadcaster m_broadc;

  public PanParmGeneral() {
    initialize();
    m_broadc = PropertyChangeBroadcaster.getInst();
    m_broadc.addPropertyChangeListener(this);
    enableVertice(false);
  }

  private void initialize() {

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 62, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JLabel lblNewLabel = new JLabel("Nome Vert.");
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
    gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
    gbc_lblNewLabel.gridx = 0;
    gbc_lblNewLabel.gridy = 0;
    add(lblNewLabel, gbc_lblNewLabel);

    m_txId = new JTextField();
    GridBagConstraints gbc_txId = new GridBagConstraints();
    gbc_txId.insets = new Insets(0, 0, 5, 0);
    gbc_txId.fill = GridBagConstraints.HORIZONTAL;
    gbc_txId.gridx = 1;
    gbc_txId.gridy = 0;
    add(m_txId, gbc_txId);
    m_txId.setColumns(10);

    m_ckStartPoint = new JCheckBox("Start");
    m_ckStartPoint.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent p_e) {
        ckStartPointClick(p_e);
      }
    });
    m_ckStartPoint.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_ckStartPoint = new GridBagConstraints();
    gbc_ckStartPoint.anchor = GridBagConstraints.WEST;
    gbc_ckStartPoint.insets = new Insets(0, 0, 5, 0);
    gbc_ckStartPoint.gridx = 1;
    gbc_ckStartPoint.gridy = 1;
    add(m_ckStartPoint, gbc_ckStartPoint);

    m_ckEndPoint = new JCheckBox("End Point");
    m_ckEndPoint.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent p_e) {
        ckEndPointClick(p_e);
      }
    });
    GridBagConstraints gbc_chkEndPoint = new GridBagConstraints();
    gbc_chkEndPoint.insets = new Insets(0, 0, 5, 0);
    gbc_chkEndPoint.anchor = GridBagConstraints.WEST;
    gbc_chkEndPoint.gridx = 1;
    gbc_chkEndPoint.gridy = 2;
    add(m_ckEndPoint, gbc_chkEndPoint);

    m_btSalvaVert = new JButton("Salva");
    m_btSalvaVert.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btSalvaVerticeClick(e);
      }
    });
    GridBagConstraints gbc_btSalvaVert = new GridBagConstraints();
    gbc_btSalvaVert.gridx = 1;
    gbc_btSalvaVert.gridy = 3;
    add(m_btSalvaVert, gbc_btSalvaVert);
  }

  protected void ckEndPointClick(ItemEvent p_e) {
    boolean bSel = p_e.getStateChange() == ItemEvent.SELECTED;
    System.out.println("PanParmGeneral.ckEndPointClick():" + bSel);
  }

  protected void ckStartPointClick(ItemEvent p_e) {
    System.out.println("PanParmGeneral.ckStartPointClick()" + p_e.toString());

  }

  protected void btSalvaVerticeClick(ActionEvent p_e) {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      //      Vertice ver = m_ver.getVertice();
      //      String newlab = m_txId.getText();
      //      s_log.debug("Salvo il vertice {}", newlab);
      //      ver.setId(newlab);
      //      if (m_ckStartPoint.isSelected() ^ ver.isStart()) {
      //        ModelloDati dati = MainFrame.getInst().getDati();
      //        dati.setStartVert(ver);
      //      }
      //      if (m_ckEndPoint.isSelected() ^ ver.isEnd()) {
      //        ModelloDati dati = MainFrame.getInst().getDati();
      //        dati.setEndVert(ver);
      //      }

    } catch (Exception e) {
      s_log.error("Err. Salva vertice", e);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    if ( ! (obj instanceof EPropChange))
      return;
    EPropChange pch = (EPropChange) obj;
    s_log.debug("Evento {} su {}", pch.toString(), p_evt.getSource().getClass().getSimpleName());
    //    Object lv = p_evt.getSource();
    //    if (lv instanceof PlotVertice) {
    //      m_ver = (PlotVertice) lv;
    //      m_bordo = null;
    //      enableVertice(true);
    //      enableBordo(false);
    //      popolaVertice();
    //    }
    //    if (lv instanceof PlotBordo) {
    //      m_ver = null;
    //      m_bordo = (PlotBordo) lv;
    //      enableVertice(false);
    //      enableBordo(true);
    //      popolaBordo();
    //    }

  }

  private void enableVertice(boolean p_b) {
    m_txId.setEnabled(p_b);
    m_ckStartPoint.setEnabled(p_b);
    m_ckEndPoint.setEnabled(p_b);
  }

  @SuppressWarnings("unused")
  private void enableBordo(boolean p_b) {
    // abilita bordo
  }

  @SuppressWarnings("unused")
  private void popolaBordo() {
    //

  }

  @SuppressWarnings("unused")
  private void popolaVertice() {
    //    var ver = m_ver.getVertice();
    //    m_txId.setText(ver.getId());
    //    m_ckStartPoint.setSelected(ver.isStart());
    //    m_ckEndPoint.setSelected(ver.isEnd());
  }

}
