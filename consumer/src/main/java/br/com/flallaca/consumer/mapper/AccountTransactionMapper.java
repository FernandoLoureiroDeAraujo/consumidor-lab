package br.com.flallaca.consumer.mapper;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.proto.AccountTransaction;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AccountTransactionMapper {

    AccountTransactionMapper INSTANCE = Mappers.getMapper(AccountTransactionMapper.class);

    // TO OBJECT JAVA
    @Mapping(source = "data", target = "dataList")
    AccountTransaction.ResponseSkeleton map(ResponseSkeletonDTO response);

    AccountTransaction.Links links(String value);

    AccountTransaction.Meta meta(String value);

}
