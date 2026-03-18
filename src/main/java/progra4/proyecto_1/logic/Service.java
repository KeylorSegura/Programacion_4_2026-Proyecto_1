package progra4.proyecto_1.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import progra4.proyecto_1.data.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private PuestoRepository puestos;
    @Autowired
    private EmpresaRepository empresas;
    @Autowired
    private UsuarioRepository usuarios;
    @Autowired
    private OferenteRepository oferentes;
    @Autowired
    private CaracteristicaRepository caracteristicas;
    @Autowired
    private PuestocaracteristicaRepository puestoCaracteristicas;

    public List<Puesto> findAll() {
        return puestos.findAll();
    }

    public List<Puesto> ultimos5Puestos() {
        List<Puesto> ultimos = puestos.findAll();
        return ultimos.stream().limit(5).toList();
    }

    public Empresa getEmpresaPorUsuario(String nombreUsuario) {
        return empresas.findByNombreUsuarioId(nombreUsuario);
    }

    public void agregarPuesto(Puesto p, List<Integer> caracteristicaIds, Map<Integer, Integer> niveles) {
        puestos.save(p);
        for (Integer cid : caracteristicaIds) {
            Caracteristica c = caracteristicas.findById(cid).orElseThrow();
            Puestocaracteristica pc = new Puestocaracteristica();
            pc.setPuesto(p);
            pc.setCaracteristica(c);
            pc.setNivel(niveles.getOrDefault(cid, 1));
            puestoCaracteristicas.save(pc);
        }
    }

    public List<Caracteristica> getCaracteristicasRaiz() {
//        List<Caracteristica> todas = caracteristicas.findAll();
//
//        return todas.stream()
//                .filter(c -> c.getPadre() != null && c.getPadre().getId().equals(c.getId()))
//                .toList();
        return caracteristicas.findRoots();
    }

    public void eliminarTodosPuestos() {
        puestos.deleteAll();
    }

    public void agregarEmpresa(Empresa empresa, String idUsuario, String clave) {
        if (usuarios.existsById(idUsuario)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        Usuario usuario = new Usuario(idUsuario, clave, "Empresa");
        usuarios.save(usuario);

        empresa.setNombreUsuario(usuario);
        empresas.save(empresa);
    }

    public boolean verificarUsuario(Usuario usuario) {
        if (!usuarios.existsById(String.valueOf(usuario.getId()))) {
            throw new IllegalArgumentException("Usuario no existe");
        }
        return true;
    }

    //Verifica si el usuario que se loggeo es de tipo "Administrador"  -Nando
    public boolean verificarAdmin(Usuario usuario) {
        Usuario verificar = usuarios.findById(usuario.getId()).orElse(null);

        if (verificar == null) {
            return false;
        }

        return Objects.equals(verificar.getTipo(), "Administrador");
    }

    public void crearUsuario(Usuario usuario) {
        if (usuarios.existsById(String.valueOf(usuario.getId()))) {
            throw new IllegalArgumentException("Usuario existe");
        }
        usuarios.save(usuario);
    }

    public List<Empresa> empresasPendientes() {
        return empresas.findByEstado(0);
    }

    public List<Oferente> oferentesPendientes() {
        return oferentes.findByEstado(0);
    }

    public List<Oferente> oferentesAutorizados() {
        return oferentes.findByEstado(1);
    }

    public void autorizarEmpresa(String id) {
        Empresa e = empresas.findById(Integer.valueOf(id)).orElseThrow();
        e.setEstado((byte) 1);
        empresas.save(e);
    }

    public void autorizarOferente(String id) {
        Oferente o = oferentes.findById(id).orElseThrow();
        o.setEstado((byte) 1);
        oferentes.save(o);
    }

    public void toggleActivoPuesto(Integer id) {
        Puesto p = puestos.findById(id).orElseThrow();
        p.setActivo(p.getActivo() == 1 ? (byte) 0 : (byte) 1);
        puestos.save(p);
    }

    public Puesto getPuestoById(Integer id) {
        return puestos.findById(id).orElseThrow();
    }

    @Transactional
    public List<Map<String, Object>> getCandidatosPuesto(Integer puestoId) {
        Puesto puesto = puestos.findById(puestoId).orElseThrow();
        List<Puestocaracteristica> requisitos = puesto.getPuestocaracteristicas().stream().toList();

        return oferentes.findAll().stream()
                .map(oferente -> {
                    long cumplidos = requisitos.stream().filter(req ->
                            oferente.getOferentecaracteristicas().stream().anyMatch(oc ->
                                    oc.getCaracteristica().getId().equals(req.getCaracteristica().getId())
                                            && oc.getNivel() >= req.getNivel()
                            )
                    ).count();


                    if (cumplidos == 0) return null;

                    int porcentaje = (int) (cumplidos * 100 / requisitos.size());

                    return Map.<String, Object>of(
                            "oferente", oferente,
                            "requisitosCumplidos", (int) cumplidos,
                            "totalRequisitos", requisitos.size(),
                            "porcentajeCoincidencia", porcentaje
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public List<Oferente> getCandidatosPuestoEstricto(Integer puestoId) {
        Puesto puesto = puestos.findById(puestoId).orElseThrow();
        List<Puestocaracteristica> requisitos = puesto.getPuestocaracteristicas().stream().toList();

        if (requisitos.isEmpty()) {
            return oferentes.findAll();
        }

        return oferentes.findAll().stream()
                .filter(oferente -> requisitos.stream().allMatch(req ->
                        oferente.getOferentecaracteristicas().stream().anyMatch(oc ->
                                oc.getCaracteristica().getId().equals(req.getCaracteristica().getId())
                                        && oc.getNivel() >= req.getNivel()
                        )
                ))
                .toList();
    }

    public Oferente getOferenteById(Integer id) {
        return oferentes.findById(String.valueOf(id)).orElseThrow();
    }
}
