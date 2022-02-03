package cn.itcast.netty.c7_application.a3_protocol.message;

public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
