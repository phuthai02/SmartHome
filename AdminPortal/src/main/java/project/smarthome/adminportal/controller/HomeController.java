package project.smarthome.adminportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
public class HomeController {

    @GetMapping("dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("login")
    public String login(@RequestParam(required = false) Integer error,
                        @RequestParam(required = false) Boolean logout,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return "login";
    }

    @GetMapping("user")
    public String user() {
        return "user";
    }
}
