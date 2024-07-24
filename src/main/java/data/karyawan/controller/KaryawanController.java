package data.karyawan.controller;

import data.karyawan.entity.Karyawan;
import data.karyawan.services.KaryawanService;
import data.karyawan.utility.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class KaryawanController {

    @Autowired
    private KaryawanService karyawanService;

    @GetMapping("/user/get")
    public ResponseEntity<MessageModel> getKaryawan(@RequestParam UUID idKaryawan) {
        return karyawanService.getDataKaryawan(idKaryawan);
    }
    @PostMapping("/user/post")
    public ResponseEntity<MessageModel> addKaryawan(@RequestBody Karyawan newKaryawan) {
        return karyawanService.addKaryawan(newKaryawan);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<MessageModel> deleteKaryawan(@RequestParam UUID idKaryawan) {
        return karyawanService.deleteKaryawan(idKaryawan);
    }

    @PutMapping("/user/update")
    public ResponseEntity<MessageModel> updateKaryawan(@RequestParam UUID idKaryawan, @RequestBody Karyawan updatedKaryawan) {
        return karyawanService.updateKaryawan(idKaryawan, updatedKaryawan);
    }

    @GetMapping("/user/browse")
    public ResponseEntity<MessageModel> browseKaryawan(@RequestParam(defaultValue = "") String nama,
                                                       @RequestParam(value = "page", defaultValue = "1") int page){
        return karyawanService.browseKaryawanService(nama,page);
    }

}
