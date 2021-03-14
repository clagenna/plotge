package prova.swing.form;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;

import sm.clagenna.plotge.swing.PanParmGeneral;

public class PanMain extends JPanel {

  /** serialVersionUID long */
  private static final long serialVersionUID = 2788270810803940698L;
  private PanParmGeneral    m_panParmGen;

  public PanMain() {
    initialize();
  }

  public PanMain(LayoutManager p_lay) {
    super(p_lay);
    initialize();
  }

  private void initialize() {

    setLayout(new BorderLayout(0, 0));

    JSplitPane splitPane = new JSplitPane();
    add(splitPane);

    m_panParmGen = new PanParmGeneral();
    splitPane.setLeftComponent(m_panParmGen);

    JPanel m_panDestro = new JPanel();
    splitPane.setRightComponent(m_panDestro);

    ButtonGroup buGroup = new ButtonGroup();

    JRadioButton radbuttVertice = new JRadioButton("Vertice");
    radbuttVertice.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setSelVertice();
      }
    });
    m_panDestro.add(radbuttVertice);
    buGroup.add(radbuttVertice);

    JRadioButton radbuttBordo = new JRadioButton("Bordo");
    radbuttBordo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setSelBordo();
      }
    });
    m_panDestro.add(radbuttBordo);
    buGroup.add(radbuttBordo);
  }

  protected void setSelVertice() {
    m_panParmGen.enableVertice();

  }

  protected void setSelBordo() {
    m_panParmGen.enableBordo();
  }
}
