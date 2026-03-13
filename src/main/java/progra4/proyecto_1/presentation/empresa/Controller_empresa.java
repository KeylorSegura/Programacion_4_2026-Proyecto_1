package progra4.proyecto_1.presentation.empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Puesto;
import progra4.proyecto_1.logic.Service;

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

    @PostMapping("/crearEmpresa")
    public String create(@ModelAttribute Empresa empresa) {
        service.agregarEmpresa(empresa);
        return "redirect:/presentation/publico/principal";
    }


    @GetMapping("/show")
    public String show(Model model) {

        model.addAttribute("puesto", new Puesto());
        service.eliminarTodosPuestos();
        return "presentation/empresa/ViewNuevoPuesto";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Puesto puesto) {

        service.agregarPuesto(puesto);

        return "redirect:/presentation/publico/principal";
    }


}