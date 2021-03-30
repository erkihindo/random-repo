package muula.pocketpuppyschooljobs.security.filters;


import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        log.info("[debug] Starting token user id verification");
        long start = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        Optional<String> userFromToken = Optional.of("12345");

        if (!userFromToken.isPresent()) {
            log.info("User is unauthorized");
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        request.setAttribute("userId", userFromToken.get());
        long time2 = System.currentTimeMillis() - start;
        log.info("[debug] Finished add authentication: " + time2);
        filterChain.doFilter(request, servletResponse);
        long time = System.currentTimeMillis() - start;
        log.info("[debug] Finished token user id verification: " + time);

    }
}
