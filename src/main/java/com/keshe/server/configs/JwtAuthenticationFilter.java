package com.keshe.server.configs;

import com.keshe.server.service.UserDetailsImpl;
import com.keshe.server.service.UserDetailsService;
import com.keshe.server.utils.DateTimeTool;
import com.keshe.server.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final UserDetailsService userDetailsService;

    @Autowired
    private RequestAttributeSecurityContextRepository repo;

    public JwtAuthenticationFilter(
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String userId = "";
        String url = request.getRequestURI();
        Date startDate = new Date();
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            SecurityContext context = SecurityContextHolder.getContext();
            repo.saveContext(context, request, response);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 提取令牌，处理可能的额外字符
            String jwt = authHeader.substring(7).trim();
            // 移除可能的引号
            if (jwt.startsWith("\"")) {
                jwt = jwt.substring(1);
            }
            if (jwt.endsWith("\"")) {
                jwt = jwt.substring(0, jwt.length() - 1);
            }
            
            // 使用JWTUtil获取用户ID（添加空值检查）
            userId = null;
            try {
                var tokenInfo = JWTUtil.getTokenInfo(jwt, JWTUtil.SECRET_KEY);
                if (tokenInfo != null) {
                    userId = tokenInfo.get("user_id", String.class);
                }
            } catch (Exception e) {
                // JWT解析失败，继续执行过滤器链
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 添加 userId 非空检查
            if (userId != null && !userId.isEmpty() && authentication == null) {
                // 根据用户ID获取用户详情
                UserDetails userDetails = null;
                try {
                    userDetails = this.userDetailsService.loadUserById(Long.parseLong(userId));
                } catch (Exception e) {
                    // 用户ID格式错误或用户不存在
                }

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // 设置用户 ID 到请求属性中
                    try {
                        request.setAttribute("userId", Long.parseLong(userId));
                    } catch (NumberFormatException e) {
                        // 用户ID格式错误
                    }
                }
            }
            SecurityContext context = SecurityContextHolder.getContext();
            repo.saveContext(context, request, response);
            filterChain.doFilter(request, response);
            Date endDate = new Date();
            double requestTime = (int) (endDate.getTime() - startDate.getTime())/1000.;
            String startTime = DateTimeTool.parseDateTime(startDate);
            logger.info(url + "," +userId+"," + startTime+ "," + requestTime);
        } catch (Exception exception) {
            exception.printStackTrace();
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
