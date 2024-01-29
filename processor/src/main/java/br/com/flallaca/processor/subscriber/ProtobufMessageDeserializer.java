package br.com.flallaca.processor.subscriber;


import br.com.flallaca.processor.proto.AccountTransaction;

public class ProtobufMessageDeserializer implements MessageDeserializer<AccountTransaction.ResponseSkeleton> {
    @Override
    public AccountTransaction.ResponseSkeleton deserialize(byte[] data) {
        try {
            return AccountTransaction.ResponseSkeleton.parseFrom(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
