package progra4.proyecto_1.presentation.oferente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import progra4.proyecto_1.logic.Service;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/presentation/oferente")
public class Controller_oferente {
    @Autowired
    private Service service;

    @GetMapping("/list/candidatos")
    public String listCandidatos(Model model){
        model.addAttribute("oferentes", service.oferentesAutorizados());
        return "presentation/empresa/ViewCandidatosPuesto";
    }
}
