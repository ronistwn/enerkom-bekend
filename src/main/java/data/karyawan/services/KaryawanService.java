package data.karyawan.services;

import data.karyawan.entity.Karyawan;
import data.karyawan.repository.KaryawanRepository;
import data.karyawan.utility.MessageModel;
import data.karyawan.utility.PaginationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static data.karyawan.utility.OtherUtils.*;

@Service
public class KaryawanService {

    @Autowired
    private KaryawanRepository karyawanRepository;

    //GET
    public ResponseEntity<MessageModel> getDataKaryawan(UUID idKaryawan) {
        MessageModel msg = new MessageModel();
        try {
            Optional<Karyawan> dataKaryawan = karyawanRepository.findById(idKaryawan);
            if (dataKaryawan.equals(null)) {
                msg.setStatus(true);
                msg.setMessage(DATA_NOT_FOUND);
                msg.setData(null);
                return ResponseEntity.ok(msg);
            } else {
                msg.setStatus(true);
                msg.setMessage(SUCCESS);
                msg.setData(dataKaryawan);
                return ResponseEntity.ok(msg);
            }
        } catch (Exception e) {
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            msg.setData(null);
            return ResponseEntity.internalServerError().body(msg);
        }
    }

    //POST
    public ResponseEntity<MessageModel> addKaryawan(Karyawan newKaryawan) {
        MessageModel msg = new MessageModel();
        try {
            Karyawan savedKaryawan = karyawanRepository.save(newKaryawan);
            msg.setStatus(true);
            msg.setMessage(SUCCESS);
            msg.setData(savedKaryawan);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            msg.setData(null);
            return ResponseEntity.internalServerError().body(msg);
        }
    }

    // DELETE
    public ResponseEntity<MessageModel> deleteKaryawan(UUID idKaryawan) {
        MessageModel msg = new MessageModel();
        try {
            if (!karyawanRepository.existsById(idKaryawan)) {
                msg.setStatus(false);
                msg.setMessage(DATA_NOT_FOUND);
                msg.setData(null);
                return ResponseEntity.badRequest().body(msg);
            }
            karyawanRepository.deleteById(idKaryawan);
            msg.setStatus(true);
            msg.setMessage(SUCCESS);
            msg.setData(null);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            msg.setData(null);
            return ResponseEntity.internalServerError().body(msg);
        }
    }

    // UPDATE
    public ResponseEntity<MessageModel> updateKaryawan(UUID idKaryawan, Karyawan updatedKaryawan) {
        MessageModel msg = new MessageModel();
        try {
            Optional<Karyawan> existingKaryawanOpt = karyawanRepository.findById(idKaryawan);
            if (existingKaryawanOpt.isEmpty()) {
                msg.setStatus(false);
                msg.setMessage(DATA_NOT_FOUND);
                msg.setData(null);
                return ResponseEntity.badRequest().body(msg);
            }

            Karyawan existingKaryawan = existingKaryawanOpt.get();
            existingKaryawan.setNama(updatedKaryawan.getNama());
            existingKaryawan.setPosisi(updatedKaryawan.getPosisi());
            existingKaryawan.setNik(updatedKaryawan.getNik());
            existingKaryawan.setIdUser(updatedKaryawan.getIdUser());

            Karyawan savedKaryawan = karyawanRepository.save(existingKaryawan);
            msg.setStatus(true);
            msg.setMessage(SUCCESS);
            msg.setData(savedKaryawan);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            msg.setData(null);
            return ResponseEntity.internalServerError().body(msg);
        }
    }

    //BROWSE
    public ResponseEntity<MessageModel> browseKaryawanService(String nama, int page) {
        ResponseEntity<MessageModel> responseEntity = ResponseEntity.ok().body(new MessageModel(false, DATA_NOT_FOUND, null));
        try {
            Integer offset = (page - 1) * LIMIT;
            Long totalData = karyawanRepository.countBrowseKaryawan(nama);
            List<Karyawan> data = karyawanRepository.browseKaryawan(nama, offset, LIMIT);
            if (data != null) {
                PaginationModel paginationModel = new PaginationModel();
                paginationModel.setCurrentPage(page);
                paginationModel.setTotalData(totalData);
                paginationModel.setTotalPages((int) Math.ceil((float) totalData / LIMIT));
                paginationModel.setLimit(LIMIT);
                paginationModel.setListData(data);
                return ResponseEntity.ok().body(new MessageModel(true, SUCCESS, paginationModel));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageModel(false, e.getMessage(), null));
        }
        return responseEntity;

    }


}

