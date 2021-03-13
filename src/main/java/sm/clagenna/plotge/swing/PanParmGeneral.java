package sm.clagenna.plotge.swing;

import java.awt.Cursor;
import java.awt.Font;
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

import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.dati.Vertice;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class PanParmGeneral extends JPanel implements PropertyChangeListener {

  private static final Logger       s_log            = LogManager.getLogger(PanParmGeneral.class);
  /** */
  private static final long         serialVersionUID = 877582887171358453L;
  private JTextField                m_txId;
  private JCheckBox                 m_ckStartPoint;
  private JCheckBox                 m_ckEndPoint;
  private PlotVertice               m_ver;
  private PlotBordo                 m_bordo;
  //   @SuppressWarnings("unused") private PlotBordo m_bordo;
  private JButton                   m_btSalvaVert;
  private PropertyChangeBroadcaster m_broadc;
  private JLabel                    m_lblBoPeso;
  private JTextField                m_txPeso;
  private JLabel                    m_txShortNo;
  private JLabel                    m_lblShortNo;
  private JButton                   m_btSalvaBordo;
  private JLabel                    m_lblTitoloParm;

  public PanParmGeneral() {
    initialize();
    m_broadc = PropertyChangeBroadcaster.getInst();
    m_broadc.addPropertyChangeListener(this);
    enableVertice(false);
  }

  private void initialize() {

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    m_lblTitoloParm = new JLabel("Nome Vertice");
    m_lblTitoloParm.setHorizontalAlignment(SwingConstants.CENTER);
    m_lblTitoloParm.setFont(new Font("Tahoma", Font.PLAIN, 15));
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
    gbc_lblNewLabel.gridx = 1;
    gbc_lblNewLabel.gridy = 0;
    add(m_lblTitoloParm, gbc_lblNewLabel);

    m_txId = new JTextField();
    GridBagConstraints gbc_txId = new GridBagConstraints();
    gbc_txId.insets = new Insets(0, 0, 5, 0);
    gbc_txId.fill = GridBagConstraints.HORIZONTAL;
    gbc_txId.gridx = 1;
    gbc_txId.gridy = 1;
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
    gbc_ckStartPoint.gridy = 2;
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
    gbc_chkEndPoint.gridy = 3;
    add(m_ckEndPoint, gbc_chkEndPoint);

    m_btSalvaVert = new JButton("Salva Vert");
    m_btSalvaVert.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btSalvaVerticeClick(e);
      }
    });
    GridBagConstraints gbc_btSalvaVert = new GridBagConstraints();
    gbc_btSalvaVert.insets = new Insets(0, 0, 5, 0);
    gbc_btSalvaVert.gridx = 1;
    gbc_btSalvaVert.gridy = 4;
    add(m_btSalvaVert, gbc_btSalvaVert);

    m_lblBoPeso = new JLabel("Peso");
    m_lblBoPeso.setHorizontalAlignment(SwingConstants.CENTER);
    m_lblBoPeso.setFont(new Font("Tahoma", Font.PLAIN, 15));
    GridBagConstraints gbc_lblBoPeso = new GridBagConstraints();
    gbc_lblBoPeso.insets = new Insets(0, 0, 5, 0);
    gbc_lblBoPeso.gridx = 1;
    gbc_lblBoPeso.gridy = 5;
    add(m_lblBoPeso, gbc_lblBoPeso);

    m_txPeso = new JTextField();
    m_txPeso.setHorizontalAlignment(SwingConstants.CENTER);
    m_txPeso.setEnabled(true);
    m_txPeso.setColumns(10);
    GridBagConstraints gbc_txPeso = new GridBagConstraints();
    gbc_txPeso.insets = new Insets(0, 0, 5, 0);
    gbc_txPeso.fill = GridBagConstraints.HORIZONTAL;
    gbc_txPeso.gridx = 1;
    gbc_txPeso.gridy = 6;
    add(m_txPeso, gbc_txPeso);

    m_lblShortNo = new JLabel("Short Path");
    GridBagConstraints gbc_lblShortNo = new GridBagConstraints();
    gbc_lblShortNo.insets = new Insets(0, 0, 5, 5);
    gbc_lblShortNo.gridx = 0;
    gbc_lblShortNo.gridy = 7;
    add(m_lblShortNo, gbc_lblShortNo);

    m_txShortNo = new JLabel("0");
    GridBagConstraints gbc_txShortNo = new GridBagConstraints();
    gbc_txShortNo.insets = new Insets(0, 0, 5, 0);
    gbc_txShortNo.gridx = 1;
    gbc_txShortNo.gridy = 7;
    add(m_txShortNo, gbc_txShortNo);

    m_btSalvaBordo = new JButton("Salva Bordo");
    m_btSalvaBordo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btSalvaBordoClick(e);
      }
    });
    GridBagConstraints gbc_btSalvaBordo = new GridBagConstraints();
    gbc_btSalvaBordo.gridx = 1;
    gbc_btSalvaBordo.gridy = 8;
    add(m_btSalvaBordo, gbc_btSalvaBordo);

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
      ModelloDati dati = MainJFrame.getInstance().getDati();
      Vertice ver = m_ver.getVertice();
      String newlab = m_txId.getText();
      s_log.debug("Salvo il vertice {}", newlab);
      // va fatto anche sui bordi pertinenti
      // ver.setId(newlab);
      dati.cambiaNomeVertice(ver, newlab);
      if (m_ckStartPoint.isSelected() ^ ver.isStart())
        dati.setStartVert(ver);

      if (m_ckEndPoint.isSelected() ^ ver.isEnd())
        dati.setEndVert(ver);

      PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.valNomeVertChanged, newlab);
    } catch (Exception e) {
      s_log.error("Err. Salva vertice", e);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

  }

  protected void btSalvaBordoClick(ActionEvent p_e) {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      String sz = m_txPeso.getText();
      Bordo bo = m_bordo.getBordo();
      Integer ii = null;
      try {
        ii = Integer.parseInt(sz);
      } catch (Exception l_e) {
        s_log.error("Il peso \"%s\" non e' interpretabile come int!", sz);
      }
      if (ii != null) {
        bo.setPeso(ii);
        PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.valPesoChanged, ii);
      }
    } catch (Exception e) {
      s_log.error("Err. Salva Bordo", e);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    Object lv = p_evt.getNewValue();
    if ( ! (obj instanceof EPropChange))
      return;
    EPropChange pch = (EPropChange) obj;
    s_log.debug("Evento {} su {}", pch.toString(), p_evt.getSource().getClass().getSimpleName());

    switch (pch) {
      case selectVertice:
        if (lv instanceof PlotVertice) {
          m_ver = (PlotVertice) lv;
          m_bordo = null;
          enableVertice();
          popolaVertice();
        }
        break;
      case selectBordo:
        if (lv instanceof PlotBordo) {
          m_ver = null;
          m_bordo = (PlotBordo) lv;
          System.out.println("PanParmGeneral.propertyChange():" + m_bordo.toString());
          enableBordo();
          popolaBordo();
        }
        break;
      default:
        break;
    }
  }

  public void enableVertice() {
    enableVertice(true);
    enableBordo(false);
  }

  private void enableVertice(boolean b_v) {
    m_txId.setVisible(b_v);
    m_ckStartPoint.setVisible(b_v);
    m_ckEndPoint.setVisible(b_v);
    m_btSalvaVert.setVisible(b_v);
  }

  public void enableBordo() {
    enableVertice(false);
    enableBordo(true);
  }

  public void enableBordo(boolean b_v) {
    m_lblBoPeso.setVisible(b_v);
    m_txPeso.setVisible(b_v);
    m_lblShortNo.setVisible(b_v);
    m_txShortNo.setVisible(b_v);
    m_btSalvaBordo.setVisible(b_v);
  }

  private void popolaVertice() {
    m_lblTitoloParm.setText("Nome Vertice");
    var ver = m_ver.getVertice();
    m_txId.setText(ver.getId());
    m_ckStartPoint.setSelected(ver.isStart());
    m_ckEndPoint.setSelected(ver.isEnd());
  }

  private void popolaBordo() {
    Bordo bo = m_bordo.getBordo();
    String sz = String.format("da %s verso %s", bo.getIdVerticeDa(), bo.getIdVerticeA());
    m_lblTitoloParm.setText(sz);
    m_txPeso.setText(String.valueOf(bo.getPeso()));
    m_txShortNo.setText(String.valueOf(bo.getShortNo()));
  }

}
