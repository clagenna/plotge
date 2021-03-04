package sm.clagenna.plotge.dati;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class ModelloDati implements Serializable, PropertyChangeListener {

  /** serialVersionUID long */
  private static final long      serialVersionUID = -3983127751832007743L;

  private static final Logger    s_log            = LogManager.getLogger(ModelloDati.class);

  @Getter
  @Setter private double         zoom;

  public ModelloDati() {
    initialize();
  }

  private void initialize() {

  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    s_log.debug("Evento {} su {}", p_evt.getPropertyName(), p_evt.getSource().getClass().getSimpleName());
  }

}
