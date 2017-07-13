package application.security;

import application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    private FilterConfig filterConfig;

    @Autowired
    JwtSettings jwtSettings;

    @Autowired
    AccessJwtToken accessJwtToken;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String authenticationToken = httpRequest.getHeader(JwtSettings.ACCESS_JWT_TOKEN_HEADER_PARAM);

        if(!httpRequest.getRequestURI().startsWith("/api/")) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            filter.doFilter(httpRequest, httpResponse);
            return;
        }

        User securityUser;

        if (authenticationToken == null || authenticationToken.equals("null")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().print("{\"errorMessage\":\"\"}");
            return;
        } else {
            securityUser = accessJwtToken.parseClaims(authenticationToken);
            request.setAttribute("securityUser", securityUser);
        }
        filter.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {

    }
}
