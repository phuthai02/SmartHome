package project.smarthome.adminportal.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.smarthome.common.utils.Constants;

@Controller
@RequestMapping()
public class HomeController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("{page}")
    public String view(@PathVariable String page, Model model, HttpServletRequest request) {
        Object userInfo = request.getSession().getAttribute(Constants.USER_INFO);
        if (userInfo != null) model.addAttribute("userInfo", userInfo);
        Resource resource = resourceLoader.getResource(String.format(Constants.Format.TEMPLATE_PAGE_PATH, page));
        if (!resource.exists()) page = "404";
        return "contents/" + page;
    }

    @GetMapping("login")
    public String login(@RequestParam(required = false) Integer error, @RequestParam(required = false) Boolean logout) {
        return "login";
    }
}
