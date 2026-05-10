package top.blogapi.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import top.blogapi.dto.request._common.LoginRequest;
import top.blogapi.dto.request._common.LoginResponse;
import top.blogapi.model.vo.Result;
import top.blogapi.service.auth.AuthService;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AuthAdminController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {

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