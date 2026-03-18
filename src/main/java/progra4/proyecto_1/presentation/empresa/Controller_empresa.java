package progra4.proyecto_1.presentation.empresa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Puesto;
import progra4.proyecto_1.logic.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/presentation/empresa")
public class Controller_empresa {
    @Autowired
    private Service service;

    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "presentation/empresa/ViewRegistroEmpresa";
    }

    @PostMapping("/crear-empresa")
    public String create(@ModelAttribute Empresa empresa,
                         @RequestParam String nombreUsuario,
                         @RequestParam String clave) {
        empresa.setEstado((byte) 0);
        service.agregarEmpresa(empresa, nombreUsuario, clave);
        return "redirect:/presentation/publico/principal";
    }

    @GetMapping("/registrar/puesto")
    public String registrarPuesto(Model model) {
        model.addAttribute("puesto", new Puesto());
        model.addAttribute("caracteristicasRaiz", service.getCaracteristicasRaiz());
        return "presentation/empresa/ViewNuevoPuesto";
    }

    @PostMapping("/crear-puesto")
    public String create(@ModelAttribute Puesto puesto,
                         @RequestParam(required = false) List<Integer> caracteristicaIds,
                         HttpServletRequest request) {
        Map<Integer, Integer> niveles = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry.getKey().startsWith("nivel_")) {
                int id = Integer.parseInt(entry.getKey().substring(6));
                int nivel = Integer.parseInt(entry.getValue()[0]);
                niveles.put(id, nivel);
            }
        }
        puesto.setActivo((byte) 1);
        puesto.setEmpresa(service.getEmpresaPorUsuario("google"));
        service.agregarPuesto(puesto, caracteristicaIds != null ? caracteristicaIds : List.of(), niveles);
        return "redirect:/presentation/publico/principal";
    }

    @GetMapping("/Dashboard")
    public String dashboard(Model model) {
        model.addAttribute("empresa", service.getEmpresaPorUsuario("google"));
        return "presentation/empresa/ViewDashboard";
    }

    @GetMapping("/ver/mis-puestos")
    public String mispuestos(Model model) {
        model.addAttribute("empresa", service.getEmpresaPorUsuario("google"));
        return "presentation/empresa/ViewVerMisPuestos";
    }

    @PostMapping("/puesto/toggle-activo/{id}")
    public String toggleActivoPuesto(@PathVariable Integer id) {
        service.toggleActivoPuesto(id);
        return "redirect:/presentation/empresa/ver/mis-puestos";
    }

    @GetMapping("/ver/candidatos/{id}")
    public String verCandidatos(Model model, @PathVariable Integer id) {
        model.addAttribute("empresa", service.getEmpresaPorUsuario("google"));
        model.addAttribute("puesto", service.getPuestoById(id));
        model.addAttribute("candidatos", service.getCandidatosPuesto(id));
        return "presentation/empresa/ViewMostrarCandidatos";
    }

}