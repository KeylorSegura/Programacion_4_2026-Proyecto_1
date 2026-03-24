package progra4.proyecto_1.presentation.oferente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Oferente;
import progra4.proyecto_1.logic.Service;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/presentation/oferente")
public class Controller_oferente {
    @Autowired
    private Service service;

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

}
