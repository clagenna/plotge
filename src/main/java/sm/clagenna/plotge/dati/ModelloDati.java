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
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.swing.PlotBordo;
import sm.clagenna.plotge.swing.PlotVertice;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class ModelloDati implements Serializable, PropertyChangeListener {

  /** serialVersionUID long */
  private static final long                             serialVersionUID = -3983127751832007743L;
  private static final Logger                           s_log            = LogManager.getLogger(ModelloDati.class);

  /** viene serializzata con GSon */
  private List<Vertice>                                 liVertici;
  private transient Map<String, Vertice>                m_mapVerts;

  @Getter
  @Setter private transient Vertice                     startVert;
  @Getter
  @Setter private transient Vertice                     endVert;
  @SuppressWarnings("unused") transient private Vertice m_veLastAdded;
  /** viene serializzata con GSon */
  private List<Bordo>                                   liBordi;

  private transient List<PlotVertice>                   m_liPVert;
  private transient List<PlotBordo>                     m_liPBord;
  @Getter
  @Setter private transient File                        fileDati;

  @Getter
  @Setter transient private double                      zoom;

  public ModelloDati() {
    initialize();
  }

  private void initialize() {

  }

  public void addVertice(Vertice p_v) {
    if (p_v == null)
      return;
    if (liVertici == null)
      liVertici = new ArrayList<Vertice>();
    if (m_mapVerts == null)
      m_mapVerts = new HashMap<>();
    m_mapVerts.put(p_v.getId(), p_v);
    if ( !liVertici.contains(p_v))
      liVertici.add(p_v);
    if (startVert == null)
      startVert = p_v;
    m_veLastAdded = p_v;
  }

  public void addBordo(Bordo p_v) {
    if (p_v == null)
      return;
    if (liBordi == null)
      liBordi = new ArrayList<Bordo>();
    if ( !liBordi.contains(p_v))
      liBordi.add(p_v);
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

  /**
   * Crea un nuovo vertice nella posizione specificata dal punto
   *
   * @param p_pu
   * @return
   */
  public PlotVertice nuovoVertice(Punto p_pu) {
    Vertice ver = new Vertice();
    ver.setPunto(p_pu);
    PlotVertice ret = new PlotVertice(ver);
    addPlotVertice(ret);
    return ret;
  }

  public Vertice findVertice(String p_id) {
    Vertice ve = null;
    if (m_mapVerts != null && m_mapVerts.containsKey(p_id))
      ve = m_mapVerts.get(p_id);
    return ve;
  }

  public PlotVertice checkBersaglioVertice(Punto p_pu) {
    PlotVertice ret = null;
    for (PlotVertice ve : getPlotVertici())
      if (ve.checkBersaglio(p_pu)) {
        ret = ve;
        break;
      }
    return ret;
  }

  public PlotBordo checkBersaglioBordo(Punto p_pu) {
    PlotBordo ret = null;
    for (PlotBordo bo : getPlotBordi())
      if (bo.checkBersaglio(p_pu)) {
        ret = bo;
        break;
      }
    return ret;
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
      PropertyChangeBroadcaster.getInst().broadCast(p_fi, EPropChange.leggiFile);
      String sz = String.format("Scritto file \"%s\"", p_fi.getAbsoluteFile());
      ModelloDati.s_log.info(sz);
    } catch (Exception l_e) {
      String sz = String.format("Errore %s salvando \"%s\"", l_e.getMessage(), p_fi.getAbsoluteFile());
      ModelloDati.s_log.error(sz);
    }
  }

  public void leggiFile(File p_fi) {
    try (JsonReader frea = new JsonReader(new FileReader(p_fi))) {
      setFileDati(p_fi);
      Gson jso = new GsonBuilder() //
          // .excludeFieldsWithoutExposeAnnotation() //
          .setPrettyPrinting() //
          .create();

      ModelloDati data = jso.fromJson(frea, ModelloDati.class);
      leggiDa(data);
      PropertyChangeBroadcaster.getInst().broadCast(p_fi, EPropChange.leggiFile);
    } catch (Exception l_e) {
      String sz = String.format("Errore %s legendo \"%s\"", l_e.getMessage(), p_fi.getAbsoluteFile());
      ModelloDati.s_log.error(sz, l_e);
    }
  }

  private void leggiDa(ModelloDati p_data) {
    m_mapVerts = new HashMap<>();
    for (Vertice ve : p_data.liVertici) {
      PlotVertice pve = new PlotVertice(ve);
      addPlotVertice(pve);
      if (pve.isStart())
        setStartVert(pve.getVertice());
      if (pve.isEnd())
        setEndVert(pve.getVertice());
      m_mapVerts.put(ve.getId(), ve);
    }

    for (Bordo bo : p_data.liBordi) {
      Vertice vDa = m_mapVerts.get(bo.getIdVerticeDa());
      Vertice vA = m_mapVerts.get(bo.getIdVerticeA());
      Bordo b2 = new Bordo(vDa, vA, bo.getPeso());
      b2.setShortNo(bo.getShortNo());
      vDa.addBordo(b2);
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
    ModelloDati.s_log.debug("Evento {} su {}", p_evt.getPropertyName(), p_evt.getSource().getClass().getSimpleName());
  }

}
