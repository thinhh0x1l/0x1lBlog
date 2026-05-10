package top.blogapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.dto.request._common.LoginRequest;
import top.blogapi.dto.request._common.LoginResponse;
import top.blogapi.model.vo.Result;
import top.blogapi.service.auth.AuthService;
import top.blogapi.util.IpAddressUtils;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;
    @PostMapping("/login")
    public Result<?> login (@RequestBody LoginRequest request, HttpServletRequest httpRequest){
//        authService.login(request, httpRequest);
//        System.out.println(IpAddressUtils.getIpAddress(request));
        LoginResponse response =
                authService.login(
                        request,
                        httpRequest
                );

        return Result.ok(
                "Đăng nhập thành công",
                response
        );
    }
}
