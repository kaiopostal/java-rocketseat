package br.com.kaiopostal.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.kaiopostal.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPat = request.getServletPath();

        if (servletPat.startsWith("/tasks")){
            var authorization = request.getHeader("Authorization");

            var userDataEnconded = authorization.substring("Basic".length()).trim();

            byte[] userDataDecode= Base64.getDecoder().decode(userDataEnconded);

            var userDataString = new String(userDataDecode);

            String[] credentials =  userDataString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null){
                response.sendError(401);
            }

            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

            if (passwordVerify.verified){
                request.setAttribute("idUser", user.getId());
                filterChain.doFilter(request, response);
            }
        }else {
            filterChain.doFilter(request, response);
        }

    }
}
