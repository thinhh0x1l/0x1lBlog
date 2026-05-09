package top.blogapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.model.vo.LoginInfo;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.LoginOrchestrator;
import top.blogapi.util.IpAddressUtils;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class LoginController {
    LoginOrchestrator loginOrchestrator;

    @PostMapping("/login")
    public Result<?> login (@RequestBody LoginInfo loginInfo, HttpServletRequest request){

        System.out.println(IpAddressUtils.getIpAddress(request));
        return Result.ok("Đăng nhập thành công!", loginOrchestrator.handleLogin(loginInfo));
    }
}
