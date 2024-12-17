package com.kma.security;

import com.kma.models.entityInfo;
import com.kma.repository.entities.User;
import com.kma.utilities.userInfoUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    userInfoUtil userUtil;

    public String generateToken(User user) throws Exception{
        //properties => claims
        Map<String, Object> claims = new HashMap<>();

        // username
        claims.put("userName", user.getUserName());

        // accountId
        claims.put("accountId", user.getUserId());

        // Info
        entityInfo info = userUtil.getInfoOfEntity(user);
        Object entityId = info.getEntityId();
        String avaFileCode = info.getAvaFileCode();

        claims.put("avaFileCode", avaFileCode); // Lưu avatar fileCode trong JWT
        claims.put("entityId", entityId); // Lưu entityId trong JWT
        try {
            return Jwts.builder()
                    .setClaims(claims) //how to extract claims from this ?  Payload
                    .setSubject(user.getUserName())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .claim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e) {
            //you can "inject" Logger, instead System.out.println
            throw new Exception("Cannot create jwt token, error: "+e.getMessage());
            //return null;
        }
    }

    private Key getSignInKey() {
        try {
            byte[] bytes = Decoders.BASE64.decode(secretKey);  // Giải mã secretKey từ Base64
            return Keys.hmacShaKeyFor(bytes);  // Tạo key sử dụng HMAC-SHA256
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate signing key", e);
        }
    }


    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())  // Lấy key từ secretKey
                    .build()
                    .parseClaimsJws(token)  // Giải mã token và lấy claims
                    .getBody();
        } catch (ExpiredJwtException e) {
            // Token đã hết hạn, ném ra lỗi rõ ràng
            throw new IllegalArgumentException("JWT token has expired", e);  // Ném lỗi hết hạn token
        } catch (JwtException e) {
            // Các lỗi khác liên quan đến JWT (ví dụ: chữ ký không hợp lệ)
            throw new IllegalArgumentException("Invalid JWT token", e);
        } catch (Exception e) {
            // Các lỗi không mong muốn khác
            throw new IllegalStateException("Error processing JWT token", e);
        }
}


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = this.extractAllClaims(token); // Giải mã token và lấy tất cả claims
            return claimsResolver.apply(claims);  // Trả về giá trị của claim đã được giải mã
        } catch (ExpiredJwtException e) {
            // Token hết hạn, ném lỗi thông báo rõ ràng
            throw new IllegalArgumentException("JWT token has expired", e);
        } catch (JwtException e) {
            // Lỗi liên quan đến token không hợp lệ
            throw new IllegalArgumentException("Invalid JWT token", e);
        } catch (Exception e) {
            // Các lỗi khác
            throw new IllegalStateException("Error processing JWT token", e);
        }
    }

    public String extractUserName(String token) {
        try {
            // Lấy username từ claim 'sub'
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            // Nếu token hết hạn, ném lỗi rõ ràng
            throw new IllegalArgumentException("JWT token has expired", e);  // Ném lỗi hết hạn token
        } catch (JwtException e) {
            // Nếu token không hợp lệ (chữ ký sai, hoặc cấu trúc không hợp lệ)
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    //check expiration
    public boolean isTokenExpired(String token) {
        try{
            Date expirationDate = this.extractClaim(token, Claims::getExpiration);
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            // Nếu token hết hạn, ném lỗi rõ ràng
            throw new IllegalArgumentException("JWT token has expired", e);  // Ném lỗi hết hạn token
        } catch (JwtException e) {
            // Nếu token không hợp lệ (chữ ký sai, hoặc cấu trúc không hợp lệ)
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()))
                && !isTokenExpired(token); //check hạn của token
    }

    // Lấy thời gian hết hạn của JWT
    public long getExpirationTime(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis(); // TTL = thời gian hết hạn - thời gian hiện tại
    }

    // Kiểm tra xem JWT có nằm trong Blacklist không
    public boolean isTokenBlacklisted(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            return false; // Token null hoặc rỗng không thể bị blacklist
        }
        String redisKey = "blacklist:jwt";
        // Kiểm tra xem JWT có tồn tại trong Set không
        Boolean isMember = redisTemplate.opsForSet().isMember(redisKey, jwt);
        return Boolean.TRUE.equals(isMember); // Trả về true nếu token tồn tại
    }

    public void addTokenToBlacklist(String jwt, long expirationTimeInMillis) {
        String redisKey = "blacklist:jwt";
        // Thêm JWT vào Redis Set
        redisTemplate.opsForSet().add(redisKey, jwt);

        // Đặt TTL cho Set, tương ứng với thời gian hết hạn lâu nhất của token
        redisTemplate.expire(redisKey, Duration.ofMillis(expirationTimeInMillis));
        System.out.println("JWT token added to blacklist: " + jwt);
    }
}