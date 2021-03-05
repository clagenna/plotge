package sm.clagenna.plotge.swing;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;

import sm.clagenna.plotge.dati.TrasponiFinestra;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class PanelBase extends JPanel {

  /** serialVersionUID long */
  private static final long                            serialVersionUID = 5653594504599665379L;
  @SuppressWarnings("unused") private TrasponiFinestra m_trasp;

  public PanelBase() {
    initialize();
  }

  public PanelBase(LayoutManager p_layout) {
    super(p_layout);
  }

  private void initialize() {
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent p_e) {
        locComponentResized(p_e);
      }
    });
    m_trasp = new TrasponiFinestra(getSize());
  }

  protected void locComponentResized(ComponentEvent p_e) {
    Dimension dim = p_e.getComponent().getSize();
    PropertyChangeEvent ev = new PropertyChangeEvent(this, null, EPropChange.panelRezized, dim);
    PropertyChangeBroadcaster.getInst().broadCast(ev);
  }

}
