package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class LoginController {
    private final UserServiceImpl userService;

    @GetMapping
    public String login() {
        return "123456";
    }
}
