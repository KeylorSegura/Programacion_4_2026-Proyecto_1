package progra4.proyecto_1.presentation.publico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import progra4.proyecto_1.logic.Puesto;
import progra4.proyecto_1.logic.Service;

@Controller
@RequestMapping("/presentation/publico")
public class Controller_publico {

    @Autowired
    private Service service;

    @GetMapping("/principal")
    public String List(Model model) {

        model.addAttribute("puestos", service.ultimos5Puestos());

        return "presentation/publico/ViewPrincipal";
    }

    @GetMapping("/show")
    public String show(Model model) {

        model.addAttribute("puesto", new Puesto());

        return "presentation/publico/Pruebaaa";
    }

//    @PostMapping("/create")
//    public String create(@ModelAttribute Puesto puesto) {
//
//        service.agregarPuesto(puesto);
//
//        return "redirect:/presentation/publico/principal";
//    }
}