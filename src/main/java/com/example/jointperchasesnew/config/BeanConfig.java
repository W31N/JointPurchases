package com.example.jointperchasesnew.config;

import com.example.jointperchasesnew.model.entity.UserOrder;
import com.example.jointperchasesnew.representation.UserOrderRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(UserOrder.class, UserOrderRepresentation.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUsername(), UserOrderRepresentation::setUsername);
            mapper.map(src -> src.getGroupPurchase().getProductName(), UserOrderRepresentation::setProductName);
        });

        return modelMapper;
    }
}