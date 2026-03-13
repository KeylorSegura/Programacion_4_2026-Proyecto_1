package progra4.proyecto_1.presentation.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import progra4.proyecto_1.logic.Service;
import progra4.proyecto_1.logic.Usuario;


@Controller
@RequestMapping("/presentation/login")
public class Controller_login {
    @Autowired
    private Service service;

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "presentation/login/ViewLogin";
    }

    @GetMapping("/iniciarSesion")
    public String iniciarSesion(@ModelAttribute Usuario usuario){
        boolean isCorrect = service.verificarUsuario(usuario);
        if(isCorrect){
            return "redirect:/presentation/publico/principal";
        } else {
            return "/presentation/login";
        }
    }

    @PostMapping("/crearCuenta")
    public String crearCuenta(@ModelAttribute Usuario usuario){
        service.crearUsuario(usuario);
        return "redirect:/presentation/publico/principal";
    }
}