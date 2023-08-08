package com.sanil.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private  JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    //Authorization

        String requestHeader = request.getHeader("Authorization");
        //Bearer 283kjseiqwhiueqwndjHWEHDOWHEDsnd   -> this type of Header we will get in which Bearer part is not the token part so, we have to remove this
        logger.info("Header : {} ",requestHeader);

        String userName = null;
        String token = null;

        if(requestHeader != null && requestHeader.startsWith("Bearer")) {

            //if control comes inside this method means every thing look good!!

            //remove "Bearer" word from the token
            token = requestHeader.substring(7);

            try {
                //we have to take the help of JWTHelper to get the userName from token
                userName = this.jwtHelper.getUsernameFromToken(token);
            }
            catch (IllegalArgumentException e) {
                logger.info("IllegalArgument while fetching user name");
                e.printStackTrace();
            }
            catch (ExpiredJwtException e) {
                logger.info("JWT Given Token is Expired !!");
                e.printStackTrace();
            }
            catch (MalformedJwtException e) {
                logger.info("Some changes has been Done in Token !! Invalid Token");
                e.printStackTrace();
            }
            catch (Exception e) {
            }
        }
        else {
            logger.info("Invalid Header value");
        }

    if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null)
    {
        //fetch user details from username
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
        Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
        if (validateToken)
        {
            //set the authentication, in this case we are using implementation class of Authentication which is UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }else{
            logger.info("Validation fails !!");
        }
    }
    filterChain.doFilter(request,response);
    }
}
