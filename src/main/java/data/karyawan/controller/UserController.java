package data.karyawan.controller;

import data.karyawan.Pojo.Login;
import data.karyawan.Pojo.Regristrasi;
import data.karyawan.Pojo.ReqRes;
import data.karyawan.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@ModelAttribute Regristrasi signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@ModelAttribute Login signInRequest) {
        return authService.signIn(signInRequest);
    }
}
