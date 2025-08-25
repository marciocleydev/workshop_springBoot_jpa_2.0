package com.myProject.SpringSalesApp.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    public static <O, D > D parseObject(O origin, Class<D> destination){
        return mapper.map(origin,destination);
    }
    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D>destination){
        List<D> list = new ArrayList<>();
        for(O object: origin){
            list.add(mapper.map(object,destination));
        }
        return list;
    }
}
