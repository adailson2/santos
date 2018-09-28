package com.adailsonsantos.cursomc.services.validation;

import com.adailsonsantos.cursomc.domain.Cliente;
import com.adailsonsantos.cursomc.domain.enums.TipoCliente;
import com.adailsonsantos.cursomc.dto.ClienteNewDTO;
import com.adailsonsantos.cursomc.repositories.ClienteRepository;
import com.adailsonsantos.cursomc.resources.exceptions.FieldMessage;
import com.adailsonsantos.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
    @Autowired
    private ClienteRepository repo;

    @Override
    public void initialize(ClienteInsert ann){
    }

    @Override
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }
        if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }
        Cliente aux = repo.findByEmail(objDto.getEmail());
        if(aux != null){
            list.add(new FieldMessage("email", "Email já existente"));
        }
        for(FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                .addConstraintViolation();
        }
        return list.isEmpty();

    }
}