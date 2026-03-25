package progra4.proyecto_1.presentation.oferente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/presentation/oferente")
@SessionAttributes("ruta")
public class Controller_oferente {
    @Autowired
    private Service service;

    @ModelAttribute("ruta")
    public List<Caracteristica> initRuta() {
        return new ArrayList<>();
    }

    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("oferente", new Oferente());
        return "presentation/publico/ViewRegistroOferente";
    }

    @PostMapping("/registrar")
    public String create(@ModelAttribute Oferente oferente,
                         @RequestParam String nombreUsuario,
                         @RequestParam String clave) {
        oferente.setEstado((byte) 0);
        service.agregarOferente(oferente, nombreUsuario, clave);
        return "redirect:/presentation/publico/principal";
    }

    @GetMapping("/habilidades")
    public String verHabilidades(
            @AuthenticationPrincipal(expression = "usuario") Usuario usuario,
            @RequestParam(required = false) Integer seleccion,
            @RequestParam(required = false) Integer rutaIndex,
            @ModelAttribute("ruta") List<Caracteristica> ruta,
            Model model) {

        if (seleccion != null) {
            Caracteristica c = service.getCaracteristicaById(seleccion);
            ruta.add(c);
        }

        if (rutaIndex != null) {
            ruta = new ArrayList<>(ruta.subList(0, rutaIndex));
        }

        List<Caracteristica> actuales;

        if (ruta.isEmpty()) {
            actuales = service.getCaracteristicasRaiz();
        } else {
            Caracteristica ultima = ruta.get(ruta.size() - 1);
            actuales = service.getHijos(ultima.getId());
        }


        List<Map<String, Object>> habilidades =
                service.getHabilidadesConRuta(usuario);



        model.addAttribute("habilidades", habilidades);
        model.addAttribute("caracteristicasActuales", actuales);
        model.addAttribute("ruta", ruta);

        return "presentation/oferente/ViewMisHabilidades";
    }

    @PostMapping("/agregarHabilidad")
    public String agregarHabilidad(
            @AuthenticationPrincipal(expression = "usuario") Usuario usuario,
            @RequestParam Integer caracteristicaId,
            @RequestParam Integer nivel) {


        service.agregarHabilidad(usuario, caracteristicaId, nivel);

        return "redirect:/presentation/oferente/habilidades";
    }
}
