package progra4.proyecto_1.logic;

import org.springframework.beans.factory.annotation.Autowired;
import progra4.proyecto_1.data.EmpresaRepository;
import progra4.proyecto_1.data.PuestoRepository;
import progra4.proyecto_1.data.UsuarioRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private PuestoRepository puestos;
    private EmpresaRepository empresas;
    private UsuarioRepository usuarios;

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

    public void agregarEmpresa(Empresa empresa) {
        if(empresas.existsById(String.valueOf(empresa.getId()))){
            throw new IllegalArgumentException("Empresa ya existe");
        }
        empresas.save(empresa);
    }

    public boolean verificarUsuario(Usuario usuario) {
        if(!usuarios.existsById(String.valueOf(usuario.getId()))){
            throw new IllegalArgumentException("Usuario no existe");
        }
        return true;
    }

    public void crearUsuario(Usuario usuario) {
        if(usuarios.existsById(String.valueOf(usuario.getId()))){
            throw new IllegalArgumentException("Usuario existe");
        }
        usuarios.save(usuario);
    }
}
