package project.smarthome.adminportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
public class HomeController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("{page}")
    public String view(@PathVariable String page) {
        Resource resource = resourceLoader.getResource("classpath:/templates/contents/" + page + ".html");
        if (!resource.exists()) page = "404";
        return "contents/" + page;
    }

    @GetMapping("login")
    public String login(@RequestParam(required = false) Integer error, @RequestParam(required = false) Boolean logout) {
        return "login";
    }
}
