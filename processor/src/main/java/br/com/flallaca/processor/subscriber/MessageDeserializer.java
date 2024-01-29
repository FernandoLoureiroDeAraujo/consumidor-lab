package br.com.flallaca.processor.subscriber;

public interface MessageDeserializer<T> {
    T deserialize(byte[] data);
}
