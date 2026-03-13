package progra4.proyecto_1.logic;

import org.springframework.beans.factory.annotation.Autowired;
import progra4.proyecto_1.data.PuestoRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private PuestoRepository puestos;

    public List<Puesto> findAll(){
        return puestos.findAll();
    }

    public List<Puesto> ultimos5Puestos() {
        List<Puesto> ultimos = puestos.findAll();
        return ultimos.stream().limit(5).toList();
    }

    public void agregarPuesto(Puesto p){
        if (puestos.existsById(p.getId())) {
            throw new IllegalArgumentException("Puesto ya existe");
        }
        puestos.save(p);
    }

    public void eliminarTodosPuestos(){
        puestos.deleteAll();
    }
}
