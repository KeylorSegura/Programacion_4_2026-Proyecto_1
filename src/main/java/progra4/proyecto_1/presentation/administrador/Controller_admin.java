package progra4.proyecto_1.presentation.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Oferente;
import progra4.proyecto_1.logic.Service;


@Controller
@RequestMapping("/presentation/admin")
public class Controller_admin {

    @Autowired
    private Service service;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "presentation/admin/ViewDashboard";
    }

    @GetMapping("/empresasPendientes")
    public String empresasPendientes(Model model){
        model.addAttribute("empresas", service.empresasPendientes());
        return "presentation/admin/ViewEmpresasPendientes";
    }

    @GetMapping("/oferentesPendientes")
    public String oferentesPendientes(Model model){
        model.addAttribute("oferentes", service.oferentesPendientes());
        return "presentation/admin/ViewOferentesPendientes";
    }

    @PostMapping("/autorizarEmpresa/{id}")
    public String autorizarEmpresa(@PathVariable String id){
        service.autorizarEmpresa(id);
        return "redirect:/presentation/admin/empresasPendientes";
    }

    @PostMapping("/autorizarOferente/{id}")
    public String autorizarOferente(@PathVariable String id){
        service.autorizarOferente(id);
        return "redirect:/presentation/admin/oferentesPendientes";
    }
}
