package com.example.demo.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntroSpectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.request.RefreshTokenRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntroSpectResponse;
import com.example.demo.entity.InvalidatedToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.InvalidatedTokenRepository;
import com.example.demo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public IntroSpectResponse introspect(IntroSpectRequest introSpectRequest) throws JOSEException, ParseException {
        String token = introSpectRequest.getToken();

        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException appException) {
            isValid = false;
        }

        return IntroSpectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token, boolean isValid) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isValid)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        SignedJWT signToken = verifyToken(logoutRequest.getToken(), true);

        String jti = signToken.getJWTClaimsSet().getJWTID();

        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signJwt = verifyToken(request.getToken(), true);

        var jti = signJwt.getJWTClaimsSet().getJWTID();
        var expiryTime = signJwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();

        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String token = generateToken(user);

        return AuthenticationResponse.builder().authenticated(true).token(token).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        log.info("==================");
        log.info("Signer Key: " + SIGNER_KEY);
        log.info("==================");
        User user = userRepository
                .findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        // không đúng username và pass để xác thực thì quăng lỗi
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // đúng rồi thì tạo cho 1 cái token để có thể thực hiện nhiều request và phải kèm theo token
        String token = generateToken(user);

        return AuthenticationResponse.builder().authenticated(true).token(token).build();
    }

    // nhận user người dùng để tạo token, do post user là mặc định được public nên được phép tạo user thoải mái
    // và sau khi tạo thaành công user thì mặc định người đó có role: USER (enum default)
    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // ở đây để sub là username để qua bên kia getInfo thì lấy cái sub này
                // mà tra
                .issuer("gialongchuai.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user)) // dùng enum USER mặc định để build scope
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token!");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName()); // với mỗi role thì add cái role đó vào scopy
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions()
                            .forEach(
                                    permission -> stringJoiner.add(permission.getName()) // đồng thời add thêm cái role
                                    // có những permissionName thì add luôn (ví dụ admin thì ngoài)
                                    // add admin thì add luôn mấy cái permission của admin như (CRUD) ...
                                    );
                }
            });
        }
        return stringJoiner.toString();
    }
}
