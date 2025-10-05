package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.bean.Result;
import top.blogapi.service.UserServiceImpl;
@Slf4j
@RestController
@RequestMapping("/admin")
public class LoginController {
    @GetMapping
    public Result index(){
        Result result = Result.ok("Thành công",Result.ok("22",Result.ok("22",null)));
        return result;
    }

}
