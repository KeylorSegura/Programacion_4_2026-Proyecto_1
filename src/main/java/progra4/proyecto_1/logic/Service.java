package progra4.proyecto_1.logic;

import org.springframework.beans.factory.annotation.Autowired;
import progra4.proyecto_1.data.EmpresaRepository;
import progra4.proyecto_1.data.OferenteRepository;
import progra4.proyecto_1.data.PuestoRepository;
import progra4.proyecto_1.data.UsuarioRepository;

import java.util.List;
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

    public List<Puesto> findAll(){
        return puestos.findAll();
    }

    public List<Puesto> ultimos5Puestos() {
        List<Puesto> ultimos = puestos.findAll();
        return ultimos.stream().limit(5).toList();
    }

    public void agregarPuesto(Puesto p){
        if (puestos.existsById(String.valueOf(p.getId()))) {
            throw new IllegalArgumentException("Puesto ya existe");
        }
        puestos.save(p);
    }

    public void eliminarTodosPuestos(){
        puestos.deleteAll();
    }

    public void agregarEmpresa(Empresa empresa, String idUsuario){

        Usuario usuario = usuarios.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        empresa.setNombreUsuario(usuario);

        empresas.save(empresa);
    }

    public boolean verificarUsuario(Usuario usuario) {
        if(!usuarios.existsById(String.valueOf(usuario.getId()))){
            throw new IllegalArgumentException("Usuario no existe");
        }
        return true;
    }

    //Verifica si el usuario que se loggeo es de tipo "Administrador"  -Nando
    public boolean verificarAdmin(Usuario usuario){
        Usuario verificar = usuarios.findById(usuario.getId()).orElse(null);

        if(verificar == null){
            return false;
        }

        return Objects.equals(verificar.getTipo(), "Administrador");
    }

    public void crearUsuario(Usuario usuario) {
        if(usuarios.existsById(String.valueOf(usuario.getId()))){
            throw new IllegalArgumentException("Usuario existe");
        }
        usuarios.save(usuario);
    }

    public List<Empresa> empresasPendientes(){
        return empresas.findByEstado(0);
    }

    public List<Oferente> oferentesPendientes(){
        return oferentes.findByEstado(0);
    }
    public void autorizarEmpresa(String id){
        Empresa e = empresas.findById(Integer.valueOf(id)).orElseThrow();
        e.setEstado((byte) 1);
        empresas.save(e);
    }
    public void autorizarOferente(String id){
        Oferente o = oferentes.findById(id).orElseThrow();
        o.setEstado((byte) 1);
        oferentes.save(o);
    }
}
