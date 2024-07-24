package data.karyawan.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationModel {
    private int currentPage;
    private long totalData;
    private int totalPages;
    private int limit;
    private Object listData;
}
