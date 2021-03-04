package sm.clagenna.plotge.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.Logger;

import lombok.Getter;
import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.sys.AppProperties;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public abstract class MainJFrame extends JFrame {

  /** serialVersionUID long */
  private static final long     serialVersionUID = -1306277146009904565L;
  private static Logger         s_log;
  private static MainJFrame     s_inst;
  @Getter private AppProperties prop;
  @Getter private ModelloDati   dati;
  private Dimension             m_winDim;
  private Point                 m_winPos;

  public MainJFrame(String p_tit) {
    super(p_tit);
    if (s_inst != null)
      throw new UnsupportedOperationException(s_inst.getClass().getSimpleName() + " gia istanziata!");
    s_inst = this;
    initialize();
  }

  public static MainJFrame getInstance() {
    return s_inst;
  }

  protected void initialize() {
    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception l_e) {
      s_log.error("Set Look and Feel", l_e);
    }

    prop = new AppProperties();
    prop.openProperties();
    /* PropertyChangeBroadcaster bcst = */ new PropertyChangeBroadcaster();

    int posX = prop.getPropIntVal(AppProperties.CSZ_PROP_POSFRAME_X);
    int posY = prop.getPropIntVal(AppProperties.CSZ_PROP_POSFRAME_Y);
    int dimX = prop.getPropIntVal(AppProperties.CSZ_PROP_DIMFRAME_X);
    int dimY = prop.getPropIntVal(AppProperties.CSZ_PROP_DIMFRAME_Y);

    if ( (dimX * dimY) > 0) {
      m_winDim = new Dimension(dimX, dimY);
      this.setPreferredSize(m_winDim);
    }
    if ( (posX * posY) != 0) {
      m_winPos = new Point(posX, posY);
      this.setLocation(m_winPos);
    }

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent p_e) {
        locComponentResized(p_e);
      }

      @Override
      public void componentMoved(ComponentEvent p_e) {
        locComponentMoved(p_e);
      }
    });

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    creaComponents();
    pack();
    setLocationRelativeTo(null);
    if (m_winPos != null)
      setLocation(m_winPos);
    else
      System.out.println("winPos e' *NULL* !?!?");

    var frame = this;
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        frame.setVisible(true);
      }
    });
  }

  protected void locComponentMoved(ComponentEvent p_e) {
    m_winPos = p_e.getComponent().getLocation();
    // System.out.println("MainJFrame.locComponentMoved():" + p_e.toString());
  }

  protected void locComponentResized(ComponentEvent p_e) {
    m_winDim = p_e.getComponent().getSize();
    // System.out.println("MainJFrame.locComponentResized():" + m_winDim.toString());
  }

  protected static void lanciaApp(JFrame p_fra) {
    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception l_e) {
      l_e.printStackTrace();
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        p_fra.setVisible(true);
      }
    });

  }

  protected abstract void creaComponents();

}
