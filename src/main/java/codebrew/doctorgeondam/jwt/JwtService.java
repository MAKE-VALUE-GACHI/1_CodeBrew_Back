package codebrew.doctorgeondam.jwt;

import codebrew.doctorgeondam.controller.dto.ApiResponse;
import codebrew.doctorgeondam.exception.AuthException;
import codebrew.doctorgeondam.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService extends OncePerRequestFilter implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;

    public JwtService(ObjectMapper objectMapper, @Lazy UserDetailsService userDetailsService) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.secret:mySecretKey}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24시간 (밀리초)
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7일 (밀리초)
    private Long refreshExpiration;

    // JWT 토큰 생성 및 검증 관련 메서드들
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            throw AuthException.ACCESS_TOKEN_EXPIRED;
        } catch (JwtException e) {
            throw AuthException.INVALID_TOKEN;
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (ExpiredJwtException e) {
            throw AuthException.ACCESS_TOKEN_EXPIRED;
        } catch (JwtException e) {
            throw AuthException.INVALID_TOKEN;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw AuthException.ACCESS_TOKEN_EXPIRED;
        } catch (JwtException e) {
            throw AuthException.INVALID_TOKEN;
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (AuthException e) {
            // 이미 적절한 예외로 변환되었으므로 재던짐
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            claims.put("id", customUserDetails.getId());
            claims.put("name", customUserDetails.getName());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole().name());
        }
        return createToken(claims, userDetails.getUsername(), jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (AuthException e) {
            // 토큰 검증 실패
            return false;
        }
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("id", Long.class);
        } catch (ExpiredJwtException e) {
            throw AuthException.ACCESS_TOKEN_EXPIRED;
        } catch (JwtException e) {
            throw AuthException.INVALID_TOKEN;
        }
    }

    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("role", String.class);
        } catch (ExpiredJwtException e) {
            throw AuthException.ACCESS_TOKEN_EXPIRED;
        } catch (JwtException e) {
            throw AuthException.INVALID_TOKEN;
        }
    }

    // JWT 필터 로직 (OncePerRequestFilter 구현)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = extractUsername(jwtToken);
            } catch (AuthException e) {
                log.warn("JWT Token 검증 실패: {} - {}", e.getErrorCode().getCode(), e.getMessage());
                handleJwtException(response, e);
                return;
            } catch (Exception e) {
                log.error("JWT Token을 파싱할 수 없습니다", e);
                handleJwtException(response, AuthException.INVALID_TOKEN);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (AuthException e) {
                log.warn("사용자 조회 실패: {} - {}", e.getErrorCode().getCode(), e.getMessage());
                handleJwtException(response, e);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    // JWT 인증 예외 처리 (AuthenticationEntryPoint 구현)
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ACCESS;
        handleJwtException(response, AuthException.UNAUTHORIZED_ACCESS);
    }

    /**
     * JWT 관련 예외를 ApiResponse 형태로 응답
     */
    private void handleJwtException(HttpServletResponse response, AuthException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exception.getErrorCode().getHttpStatus().value());

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                null,
                new ApiResponse.ErrorInfo(
                        exception.getErrorCode().getCode(),
                        exception.getMessage()
                )
        );

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
