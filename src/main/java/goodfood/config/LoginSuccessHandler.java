package goodfood.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        clearSession(request);
        String uri = setPrevPageToSession(request, response);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 사용자의 권한 목록

        HttpSession session = request.getSession();
        String username = authentication.getName();

        session.setAttribute("username", username);
        session.setAttribute("authorities", authorities);

        redirectStrategy.sendRedirect(request, response, uri);
    }

    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    private String setPrevPageToSession(HttpServletRequest request, HttpServletResponse response) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String uri = "/";
        String prevPage = (String) request.getSession().getAttribute("prevPage");

        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
            try {
                URI prevUri = new URI(prevPage);
                if(prevUri.getPath().contains("signup")) {
                    uri = "/";
                } else {
                    uri = prevUri.getPath();
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (savedRequest != null) {
            uri = savedRequest.getRedirectUrl();
        }
        return uri;
    }

}
