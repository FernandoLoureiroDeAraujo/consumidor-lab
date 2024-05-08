package br.com.flallaca.scheduler.dto;

import br.com.flallaca.scheduler.enums.MessageBrokerType;
import br.com.flallaca.scheduler.enums.MessageFormatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolderMessageData {

    private List<String> urls;

}