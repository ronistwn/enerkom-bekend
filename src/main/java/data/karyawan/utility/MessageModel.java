package data.karyawan.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageModel {
    private String message;
    private boolean status;
    private Object data;
    public MessageModel() {
    }
    public MessageModel(boolean status, String message, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
    public MessageModel(boolean status, String message) {
        this.message = message;
        this.status = status;
    }
}

