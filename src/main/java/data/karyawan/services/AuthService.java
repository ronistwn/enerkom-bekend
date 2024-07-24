package data.karyawan.services;

import data.karyawan.Pojo.Login;
import data.karyawan.Pojo.Regristrasi;
import data.karyawan.Pojo.ReqRes;
import data.karyawan.entity.User;
import data.karyawan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository ourUserRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<ReqRes> signUp(Regristrasi registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            if (ourUserRepository.existsByEmail(registrationRequest.getEmail())) {
                resp.setMessage("Email is already registered");
                resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(resp);
            }

            User ourUser = new User();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());

            User ourUserResult = ourUserRepository.save(ourUser);

            var jwt = jwtUtils.generateToken(ourUserResult);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), ourUserResult);

            if (ourUserResult.getId() > 0) {
                resp.setUsers(ourUserResult);
                resp.setMessage("User saved successfully");
                resp.setStatusCode(HttpStatus.OK.value());
                resp.setToken(jwt);
                resp.setRefreshToken(refreshToken);
                resp.setExpirationTime("24Hr");
                return ResponseEntity.ok().body(resp);
            }
            return ResponseEntity.ok().body(resp);
        } catch (Exception e) {
            resp.setMessage(e.getMessage());
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.internalServerError().body(resp);
        }
    }

    public ResponseEntity<ReqRes> signIn(Login signInRequest) {
        ReqRes resp = new ReqRes();
        try {
            var user = ourUserRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("Email not found")
            );
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            System.out.println("USER IS: " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            resp.setMessage("Successfully Signed In");
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setName(user.getName());
            resp.setToken(jwt);
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hr");
            return ResponseEntity.ok().body(resp);
        } catch (BadCredentialsException e) {
            resp.setMessage("Wrong Password");
            resp.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.internalServerError().body(resp);
        } catch (UsernameNotFoundException e) {
            resp.setMessage("Email not found");
            resp.setStatusCode(HttpStatus.NOT_FOUND.value());

            return ResponseEntity.internalServerError().body(resp);
        }
    }

    public ResponseEntity<ReqRes> refreshToken(ReqRes refreshTokenRequest) {
        ReqRes resp = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        User users = ourUserRepository.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            resp.setMessage("Successfully Refreshed Token");
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setToken(jwt);
            resp.setRefreshToken(refreshTokenRequest.getToken());
            resp.setExpirationTime("24Hr");
            return ResponseEntity.ok().body(resp);
        } else {
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.internalServerError().body(resp);
        }
    }
}
