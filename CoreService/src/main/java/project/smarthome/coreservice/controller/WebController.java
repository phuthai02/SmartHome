package project.smarthome.coreservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/smarthome-core/home";
        }

        if (error != null) {
            if ("true".equals(error)) {
                model.addAttribute("errorMessage", "Invalid username or password.");
            } else if ("access_denied".equals(error)) {
                model.addAttribute("errorMessage", "Access denied. You don't have permission to access this resource.");
            } else if ("token_invalid".equals(error)) {
                model.addAttribute("errorMessage", "Your session has expired. Please login again.");
            }
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }

        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("username", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());
            return "home";
        }

        return "redirect:/smarthome-core/login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/smarthome-core/home";
    }
}