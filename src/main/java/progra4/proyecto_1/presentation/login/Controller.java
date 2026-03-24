package progra4.proyecto_1.presentation.login;

import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/login")
    public String login() {
        return "presentation/login/ViewLogin";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "presentation/login/ViewNotAuthorized";
    }
}

