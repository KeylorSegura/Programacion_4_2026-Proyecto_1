package progra4.proyecto_1.presentation.publico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import progra4.proyecto_1.logic.Caracteristica;
import progra4.proyecto_1.logic.Puesto;
import progra4.proyecto_1.logic.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/presentation/publico")
@SessionAttributes("caracteristicaIdsSeleccionadas")
public class Controller_publico {

    @Autowired
    private Service service;

    // Inicializa la lista en sesión si no existe
    @ModelAttribute("caracteristicaIdsSeleccionadas")
    public List<Integer> inicializarCaracteristicaIds() {
        return new ArrayList<>();
    }

    @GetMapping("/principal")
    public String List(Model model) {

        model.addAttribute("puestos", service.ultimos5Puestos());

        return "presentation/publico/ViewPrincipal";
    }

    @GetMapping("/puestos")
    public String mostrarBusqueda(Model model,
                                  @ModelAttribute("caracteristicaIdsSeleccionadas") List<Integer> caracteristicaIdsSeleccionadas) {

        List<Caracteristica> caracteristicasRaiz = service.getCaracteristicasRaiz();
        service.marcarArbolAbierto(caracteristicasRaiz, caracteristicaIdsSeleccionadas);
        model.addAttribute("caracteristicasRaiz", service.getCaracteristicasRaiz());

        if (!caracteristicaIdsSeleccionadas.isEmpty()) {
            List<Puesto> puestos = service.buscarPorCaracteristicas(caracteristicaIdsSeleccionadas);
            model.addAttribute("puestos", puestos);
        }

        return "presentation/publico/ViewBuscarPuestos";
    }

    @PostMapping("/filtrar")
    public String filtrar(@RequestParam(value = "caracteristicaIds", required = false) List<Integer> caracteristicaIds,
                          @RequestParam("accion") String accion,
                          Model model,
                          @ModelAttribute("caracteristicaIdsSeleccionadas") List<Integer> caracteristicaIdsSeleccionadas) {

        if ("limpiar".equals(accion)) {
            caracteristicaIdsSeleccionadas.clear();
        } else if ("filtrar".equals(accion)) {
            caracteristicaIdsSeleccionadas.clear();
            if (caracteristicaIds != null) {
                caracteristicaIdsSeleccionadas.addAll(caracteristicaIds);
            }
        }

        model.addAttribute("caracteristicasRaiz", service.getCaracteristicasRaiz());


        List<Puesto> puestos = caracteristicaIdsSeleccionadas.isEmpty() ?
                new ArrayList<>() :
                service.buscarPorCaracteristicas(caracteristicaIdsSeleccionadas);

        model.addAttribute("puestos", puestos);

        return "redirect:/presentation/publico/puestos";
    }


}