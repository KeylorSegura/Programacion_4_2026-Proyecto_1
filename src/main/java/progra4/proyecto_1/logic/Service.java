package progra4.proyecto_1.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import progra4.proyecto_1.data.*;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private OferentecaracteristicaRepository oferenteCaracteristicas;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Puesto> findAll() {
        return puestos.findAll();
    }

    public List<Puesto> ultimos5Puestos() {
        return puestos.findAll().stream()
                .filter(p -> "Publica".equalsIgnoreCase(p.getTipoPublicacion()) && p.getActivo() == 1)
                .sorted((p1, p2) -> Long.compare(p2.getId(), p1.getId()))
                .limit(5)
                .toList();
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
        List<Caracteristica> raices = caracteristicas.findRoots();

        ordenarRecursivo(raices);

        return raices;
    }

    private void ordenarRecursivo(Collection<Caracteristica> nodos) {
        if (nodos == null) return;
        List<Caracteristica> lista = new ArrayList<>(nodos);
        lista.sort(Comparator.comparing(Caracteristica::getNombre));
        for (Caracteristica nodo : lista) {
            ordenarRecursivo(nodo.getCaracteristicas());
        }
    }

    public void eliminarTodosPuestos() {
        puestos.deleteAll();
    }

    public void agregarEmpresa(Empresa empresa, String idUsuario, String clave) {
        if (usuarios.existsById(idUsuario)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        Usuario usuario = new Usuario(idUsuario, passwordEncoder.encode(clave), "Empresa");
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

    public void crearCaracteristica(String nombre, Integer padreId){
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setNombre(nombre);

        if (padreId == null){
            caracteristica.setPadre(null);
            caracteristicas.save(caracteristica);
        }
        else{
            Caracteristica padre = caracteristicas.findById(padreId).orElseThrow(() -> new RuntimeException("Padre no existe"));
            caracteristica.setPadre(padre);
            caracteristicas.save(caracteristica);
        }
    }
    public List<Caracteristica> findCaracteristicas(){
        return caracteristicas.findAll();
    }

    private Set<Integer> obtenerIdsConDescendientes(Integer id) {
        Set<Integer> resultado = new HashSet<>();
        Queue<Caracteristica> cola = new LinkedList<>();

        Caracteristica raiz = caracteristicas.findById(id).orElse(null);
        if (raiz == null) return resultado;

        cola.add(raiz);

        while (!cola.isEmpty()) {
            Caracteristica actual = cola.poll();
            resultado.add(actual.getId());

            for (Caracteristica hijo : actual.getCaracteristicas()) {
                cola.add(hijo);
            }
        }

        return resultado;
    }

    public List<Puesto> buscarPorCaracteristicas(List<Integer> caracteristicaIds) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Set<Integer>> grupos = caracteristicaIds.stream()
                .map(this::obtenerIdsConDescendientes)
                .toList();

        List<Puesto> todosPuestos = puestos.findAll();

        return todosPuestos.stream()
                .filter(p -> "Publica".equalsIgnoreCase(p.getTipoPublicacion()))
                .filter(puesto -> {
                    Set<Integer> idsDelPuesto = puesto.getPuestocaracteristicas().stream()
                            .map(pc -> pc.getCaracteristica().getId())
                            .collect(Collectors.toSet());

                    return grupos.stream()
                            .allMatch(grupo -> grupo.stream().anyMatch(idsDelPuesto::contains));
                })
                .toList();
    }

    public void agregarOferente(Oferente oferente, String idUsuario, String clave) {
        if (usuarios.existsById(idUsuario)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        Usuario usuario = new Usuario(idUsuario, passwordEncoder.encode(clave), "Oferente");
        usuarios.save(usuario);

        oferente.setNombreUsuario(usuario);
        oferentes.save(oferente);
    }


    public String construirRutaCaracteristica(Caracteristica c) {
        if (c.getPadre() == null) {
            return c.getNombre();
        }
        return construirRutaCaracteristica(c.getPadre()) + " / " + c.getNombre();
    }


    public Oferente getOferenteByUsuario(Usuario usuario) {
        return oferentes.findAll().stream()
                .filter(o -> o.getNombreUsuario().getId().equals(usuario.getId()))
                .findFirst()
                .orElseThrow();
    }

    public List<Map<String, Object>> getHabilidadesConRuta(Usuario usuario) {
        Oferente oferente = getOferenteByUsuario(usuario);

        return oferente.getOferentecaracteristicas().stream()
                .sorted(Comparator.comparing(
                        oc -> construirRutaCaracteristica(oc.getCaracteristica())
                ))
                .map(oc -> Map.<String, Object>of(
                        "ruta", construirRutaCaracteristica(oc.getCaracteristica()),
                        "nivel", oc.getNivel()
                ))
                .toList();
    }

    public List<Caracteristica> getHijos(Integer padreId) {
        return caracteristicas.findAll().stream()
                .filter(c -> c.getPadre() != null &&
                        c.getPadre().getId().equals(padreId))
                .toList();
    }

    public Caracteristica getCaracteristicaById(Integer id) {
        return caracteristicas.findById(id).orElseThrow();
    }

    @Transactional
    public void agregarHabilidad(Usuario usuario, Integer caracteristicaId, Integer nivel) {
        Oferente oferente = getOferenteByUsuario(usuario);

        Caracteristica caracteristica = caracteristicas.findById(caracteristicaId)
                .orElseThrow(() -> new RuntimeException("Característica no existe"));

        Oferentecaracteristica existente = oferente.getOferentecaracteristicas()
                .stream()
                .filter(oc -> oc.getCaracteristica().getId().equals(caracteristicaId))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            existente.setNivel(nivel);
        } else {
            Oferentecaracteristica nueva = new Oferentecaracteristica();
            nueva.setOferente(oferente);
            nueva.setCaracteristica(caracteristica);
            nueva.setNivel(nivel);

            oferenteCaracteristicas.save(nueva);
        }
    }

    @Transactional
    public void guardarCV(Usuario usuario, MultipartFile archivo) throws Exception {
        Oferente oferente = getOferenteByUsuario(usuario);

        if (!archivo.isEmpty()) {
            oferente.setCurriculum(archivo.getBytes());
            oferentes.save(oferente);
        }
    }

    public byte[] obtenerCV(Usuario usuario) {
        Oferente oferente = getOferenteByUsuario(usuario);
        return oferente.getCurriculum();
    }

    public boolean existeCV(Usuario usuario){
        byte[] cv = obtenerCV(usuario);
        return (cv != null && cv.length > 0);
    }


    public void marcarArbolAbierto(List<Caracteristica> raices, List<Integer> seleccionados) {
        for (Caracteristica raiz : raices) {
            marcarNodo(raiz, seleccionados);
        }
    }

    private boolean marcarNodo(Caracteristica nodo, List<Integer> seleccionados) {
        boolean abierto = false;

        if (nodo.getCaracteristicas() != null) {
            for (Caracteristica hijo : nodo.getCaracteristicas()) {
                boolean hijoAbierto = marcarNodo(hijo, seleccionados);
                if (hijoAbierto) {
                    abierto = true;
                }
            }
        }

        if (abierto) {
            nodo.setAbierto(true);
            return true;
        }

        if (seleccionados.contains(nodo.getId())) {
            nodo.setAbierto(false);
            return true;
        }

        nodo.setAbierto(false);
        return false;
    }

}
