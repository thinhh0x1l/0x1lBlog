package top.blogapi.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.service.GuestService;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class IndexAdminController {
    GuestService guestService;
    private static final String TOKEN_HEADER = "x-guest-token";
    @GetMapping("/guest/bootstrap")
    public ResponseEntity<Void> bootstrapGuestToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String token = request.getHeader(TOKEN_HEADER);
        if (token == null || token.isBlank()) {

            String guestToken =  guestService.createGuestToken();
            System.out.println("TokenGuest: "+ guestToken );
            guestService.getGuestOrCreateByToken(guestToken);
            response.setHeader(
                    TOKEN_HEADER,
                    guestToken
            );

            response.setHeader(
                    HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                    TOKEN_HEADER
            );
        }

        return ResponseEntity.ok().build();
    }

}
