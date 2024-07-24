package data.karyawan.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "karyawan")
@Data
public class Karyawan {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "id_karyawan")
    private UUID idKaryawan;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Column(name = "posisi", nullable = false)
    private String posisi;

    @Column(name = "nik", nullable = false)
    private String nik;

    @Column(name = "idUser", nullable = false)
    private Long idUser;
}
