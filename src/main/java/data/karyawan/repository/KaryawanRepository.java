package data.karyawan.repository;

import data.karyawan.entity.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan, UUID> {

    @Query(value = "select * from data_karyawan.karyawan a " +
            "where (:nama = '' OR a.nama ILIKE '%' || :nama || '%')\n" +
            "OFFSET :offset LIMIT :limit ", nativeQuery = true)
    List<Karyawan> browseKaryawan(@Param("nama") String nama,
                                  @Param("offset") Integer offset,
                                  @Param("limit") Integer limit);
    @Query(value = "SELECT count(*) from data_karyawan.karyawan a \n" +
            "where (:nama = '' OR a.nama ILIKE '%' || :nama || '%')", nativeQuery = true)
    Long countBrowseKaryawan(@Param("nama") String nama);

}
