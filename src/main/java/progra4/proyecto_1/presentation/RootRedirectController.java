package progra4.proyecto_1.presentation;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("RootRedirectController")

public class RootRedirectController {
    @GetMapping("/")
    public String index(Model model) {return "redirect:/presentation/publico/principal";}
}
