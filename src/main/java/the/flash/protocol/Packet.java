package the.flash.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;


    /**
     * 不进行序列化getCommand
     * @return 序列化后的
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();
}
