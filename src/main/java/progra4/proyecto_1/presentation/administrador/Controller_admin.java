package progra4.proyecto_1.presentation.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.Caracteristica;
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

    @GetMapping("/empresas-pendientes")
    public String empresasPendientes(Model model){
        model.addAttribute("empresas", service.empresasPendientes());
        return "presentation/admin/ViewEmpresasPendientes";
    }

    @GetMapping("/oferentes-pendientes")
    public String oferentesPendientes(Model model){
        model.addAttribute("oferentes", service.oferentesPendientes());
        return "presentation/admin/ViewOferentesPendientes";
    }
    @GetMapping("/caracteristicas")
    public String Caracteristicas(Model model){
        model.addAttribute("caracteristicasRaiz", service.getCaracteristicasRaiz());
        model.addAttribute("caracteristicas", service.findCaracteristicas());
        return "presentation/admin/ViewCrearCaracteristicas";
    }

    @PostMapping("/caracteristicas/crear")
    public String crearCaracteristicas(
            @RequestParam String nombre,
            @RequestParam(required = false) String idPadre) {

        Integer padreId = null;

        if (idPadre != null && !idPadre.isEmpty()) {
            padreId = Integer.parseInt(idPadre);
        }

        service.crearCaracteristica(nombre, padreId);

        return "redirect:/presentation/admin/caracteristicas";
    }



    @PostMapping("/autorizarEmpresa/{id}")
    public String autorizarEmpresa(@PathVariable String id){
        service.autorizarEmpresa(id);
        return "redirect:/presentation/admin/empresas-pendientes";
    }

    @PostMapping("/autorizarOferente/{id}")
    public String autorizarOferente(@PathVariable String id){
        service.autorizarOferente(id);
        return "redirect:/presentation/admin/oferentes-pendientes";
    }




}
