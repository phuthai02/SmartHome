package project.smarthome.dataservice.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.smarthome.common.entity.User;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.user.UserService;

@Controller
@RequestMapping("user")
public class UserController extends BaseController<User, Long> {
    @Autowired
    private UserService userService;

    @Override
    protected BaseService<User, Long> getBaseService() {
        return userService;
    }

    @Override
    protected String getEntityName() {
        return "USER";
    }
}
