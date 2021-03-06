package sm.clagenna.plotge.dati;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.plotge.swing.PlotBordo;
import sm.clagenna.plotge.swing.PlotVertice;

public class ModelloDati implements Serializable, PropertyChangeListener {

  /** serialVersionUID long */
  private static final long                 serialVersionUID = -3983127751832007743L;
  private static final Logger               s_log            = LogManager.getLogger(ModelloDati.class);

  private List<Vertice>                     m_liVertici;
  private transient Map<String, Vertice>    m_mapVerts;

  @Getter
  @Setter private transient Vertice         startVert;
  @Getter
  @Setter private transient Vertice         endVert;
  transient private Vertice                 m_veLastAdded;

  private List<Bordo>                       m_liBordi;

  private transient List<PlotVertice>       m_liPVert;
  private transient List<PlotBordo>         m_liPBord;

  @Getter
  @Setter transient private double          zoom;

  public ModelloDati() {
    initialize();
  }

  private void initialize() {

  }

  public void addVertice(Vertice p_v) {
    if (p_v == null)
      return;
    if (m_liVertici == null)
      m_liVertici = new ArrayList<Vertice>();
    if (m_mapVerts == null)
      m_mapVerts = new HashMap<>();
    m_mapVerts.put(p_v.getId(), p_v);
    if ( !m_liVertici.contains(p_v))
      m_liVertici.add(p_v);
    if (startVert == null)
      startVert = p_v;
    m_veLastAdded = p_v;
  }

  public void addBordo(Bordo p_v) {
    if (p_v == null)
      return;
    if (m_liBordi == null)
      m_liBordi = new ArrayList<Bordo>();
    if ( !m_liBordi.contains(p_v))
      m_liBordi.add(p_v);
  }

  public void addPlotVertice(PlotVertice pv) {
    if (pv == null)
      return;
    getPlotVertici().add(pv);
    Vertice ve = pv.getVertice();
    addVertice(ve);
  }

  public void addPlotBordo(PlotBordo pv) {
    if (pv == null)
      return;
    getPlotBordi().add(pv);
    Bordo bo = pv.getBordo();
    addBordo(bo);
  }

  public List<PlotVertice> getPlotVertici() {
    if (m_liPVert == null)
      m_liPVert = new ArrayList<>();
    return m_liPVert;
  }

  public List<PlotBordo> getPlotBordi() {
    if (m_liPBord == null)
      m_liPBord = new ArrayList<>();
    return m_liPBord;
  }

  public void salvaFile(File p_fi) {
    try (FileWriter fwri = new FileWriter(p_fi)) {
      Gson jso = new GsonBuilder() //
          // .excludeFieldsWithoutExposeAnnotation() //
          .setPrettyPrinting() //
          .create();

      jso.toJson(this, fwri);
      String sz = String.format("Scritto file \"%s\"", p_fi.getAbsoluteFile());
      s_log.info(sz);
    } catch (Exception l_e) {
      String sz = String.format("Errore %s salvando \"%s\"", l_e.getMessage(), p_fi.getAbsoluteFile());
      s_log.error(sz);
    }
  }

  public void leggiFile(File p_fi) {
    try (JsonReader frea = new JsonReader(new FileReader(p_fi))) {
      Gson jso = new GsonBuilder() //
          // .excludeFieldsWithoutExposeAnnotation() //
          .setPrettyPrinting() //
          .create();

      ModelloDati data = jso.fromJson(frea, ModelloDati.class);
      leggiDa(data);

    } catch (Exception l_e) {
      String sz = String.format("Errore %s legendo \"%s\"", l_e.getMessage(), p_fi.getAbsoluteFile());
      s_log.error(sz, l_e);
    }
  }

  private void leggiDa(ModelloDati p_data) {
    m_mapVerts = new HashMap<>();
    for (Vertice ve : p_data.m_liVertici) {
      PlotVertice pve = new PlotVertice(ve);
      addPlotVertice(pve);
      if (pve.isStart())
        setStartVert(pve.getVertice());
      if (pve.isEnd())
        setEndVert(pve.getVertice());
      m_mapVerts.put(ve.getId(), ve);
    }

    for (Bordo bo : p_data.m_liBordi) {
      Vertice vDa = m_mapVerts.get(bo.getIdVerticeDa());
      Vertice vA = m_mapVerts.get(bo.getIdVerticeA());
      Bordo b2 = new Bordo(vDa, vA, bo.getPeso());
      b2.setShortNo(bo.getShortNo());
      addPlotBordo(new PlotBordo(b2));
    }
    controllaCiechi();
  }

  /**
   * controlla quei vertici da cui non parte nessun bordo
   */
  private void controllaCiechi() {
    for (PlotVertice pve : getPlotVertici()) {
      Vertice ve = pve.getVertice();
      var li = ve.getBordi();
      if (li == null || li.size() == 0)
        ve.setCieco(true);
    }

  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    s_log.debug("Evento {} su {}", p_evt.getPropertyName(), p_evt.getSource().getClass().getSimpleName());
  }

}
