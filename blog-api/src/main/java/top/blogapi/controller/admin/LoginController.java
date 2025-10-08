package top.blogapi.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.util.Result;

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
