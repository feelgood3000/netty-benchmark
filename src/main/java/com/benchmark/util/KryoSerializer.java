package com.benchmark.util;

import com.benchmark.pojo.UdpRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午9:58
 */
public class KryoSerializer {

    private static final ThreadLocal<Kryo> kryos=new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            Kryo kryo=new Kryo();
            kryo.register(UdpRequest.class);
            return kryo;
        }
    };

    private KryoSerializer(){

    }


    public static byte[] encode(Object object) {
        Kryo kryo = kryos.get();
        Output output = new Output(10, 65535);
        kryo.writeObject(output, object);
        output.close();
        return output.getBuffer();
    }

    public static <T> T deCode(byte[] data,Class<T> type){
        Kryo kryo = kryos.get();
        Input input = new Input(data);
        T t = kryo.readObject(input, type);
        input.close();
        return t;
    }
}
