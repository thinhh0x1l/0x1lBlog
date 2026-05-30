package top.blogapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.blogapi.context.GuestContext;
import top.blogapi.service.GuestService;
import top.blogapi.util.IpAddressUtils;
import top.blogapi.util.UserAgentUtils;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GuestTokenFilter extends OncePerRequestFilter {
    GuestService guestService;
    UserAgentUtils userAgentUtils;
    private static final String TOKEN_HEADER = "x-guest-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(IpAddressUtils.getIpAddress(request));
        String guestToken = request.getHeader(TOKEN_HEADER);
        System.out.println(guestToken);
        if (guestToken == null || guestToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute("guestToken", guestToken);

        GuestContext.set(guestService.getGuestOrCreateByToken(guestToken));
//        System.out.println(
//                userAgentUtils.parseOsAndBrowser(request.getHeader("user-agent"))
//        );
        try{
            filterChain.doFilter(request, response);
        }finally {
            GuestContext.clear();
        }
    }
}