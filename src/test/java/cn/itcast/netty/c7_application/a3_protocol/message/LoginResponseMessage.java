package cn.itcast.netty.c7_application.a3_protocol.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
