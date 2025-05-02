package Utils;

import Controllers.SessionController;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/secured/*")
@Priority(1)
public class AuthenticationFilter implements Filter {
    
    @Inject SessionController currentSession;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if the user is authenticated
        if (!currentSession.isValid()) {
            // User is not authenticated, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.xhtml");
            return;
        }

        // User is authenticated, continue with the request
        chain.doFilter(request, response);
    }

}
