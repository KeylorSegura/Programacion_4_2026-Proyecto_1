package progra4.proyecto_1.presentation.empresa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Puesto;
import progra4.proyecto_1.logic.Service;
import progra4.proyecto_1.security.UserDetailsImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/presentation/empresa")
public class Controller_empresa {
    @Autowired
    private Service service;

    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "presentation/publico/ViewRegistroEmpresa";
    }

    @PostMapping("/registrar")
    public String create(@ModelAttribute Empresa empresa,
                         @RequestParam String nombreUsuario,
                         @RequestParam String clave) {
        empresa.setEstado((byte) 0);
        service.agregarEmpresa(empresa, nombreUsuario, clave);
        return "redirect:/presentation/publico/principal";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetailsImp userDetails) {
        model.addAttribute("empresa", service.getEmpresaPorUsuario(userDetails.getUsername()));
        return "presentation/empresa/ViewDashboard";
    }

    @GetMapping("/puesto/registrar")
    public String registrarPuesto(Model model) {
        model.addAttribute("puesto", new Puesto());
        model.addAttribute("caracteristicasRaiz", service.getCaracteristicasRaiz());
        return "presentation/empresa/ViewNuevoPuesto";
    }

    @PostMapping("/puesto/registrar")
    public String create(@ModelAttribute Puesto puesto,
                         @RequestParam(required = false) List<Integer> caracteristicaIds,
                         HttpServletRequest request,
                         @AuthenticationPrincipal UserDetailsImp userDetails) {
        Map<Integer, Integer> niveles = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry.getKey().startsWith("nivel_")) {
                int id = Integer.parseInt(entry.getKey().substring(6));
                int nivel = Integer.parseInt(entry.getValue()[0]);
                niveles.put(id, nivel);
            }
        }
        puesto.setActivo((byte) 1);
        puesto.setEmpresa(service.getEmpresaPorUsuario(userDetails.getUsername()));
        service.agregarPuesto(puesto, caracteristicaIds != null ? caracteristicaIds : List.of(), niveles);
        return "redirect:/presentation/publico/principal";
    }

    @PostMapping("/puesto/toggle-activo/{id}")
    public String toggleActivoPuesto(@PathVariable Integer id) {
        service.toggleActivoPuesto(id);
        return "redirect:/presentation/empresa/puestos";
    }

    @GetMapping("/puestos")
    public String mispuestos(Model model, @AuthenticationPrincipal UserDetailsImp userDetails) {
        Empresa empresa = service.getEmpresaPorUsuario(userDetails.getUsername());
        model.addAttribute("empresa", empresa);
        model.addAttribute("puestos", empresa.getPuestos());
        return "presentation/empresa/ViewVerMisPuestos";
    }

    @GetMapping("/candidatos/puesto")
    public String verCandidatos(Model model, @RequestParam Integer id, @AuthenticationPrincipal UserDetailsImp userDetails) {
        model.addAttribute("empresa", service.getEmpresaPorUsuario(userDetails.getUsername()));
        model.addAttribute("puesto", service.getPuestoById(id));
        model.addAttribute("candidatos", service.getCandidatosPuesto(id));
        return "presentation/empresa/ViewMostrarCandidatos";
    }

    @GetMapping("/candidatos/oferente")
    public String verOferente(Model model, @RequestParam Integer id, @AuthenticationPrincipal UserDetailsImp userDetails) {
        model.addAttribute("empresa", service.getEmpresaPorUsuario(userDetails.getUsername()));
        model.addAttribute("oferente", service.getOferenteById(id));
        return "presentation/empresa/ViewDetallesCandidatos";
    }

    @GetMapping("/candidatos/oferente/verCV")
    public ResponseEntity<byte[]> verCVOferente(@RequestParam Integer id) {
        byte[] pdf = service.getOferenteById(id).getCurriculum();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=cv.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}