package base.utility.module.utilities.entities;

import base.utility.module.utilities.exceptions.LinkNotFoundException;
import io.micronaut.data.repository.CrudRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static base.utility.module.utilities.responses.ResponseConstants.MESSAGE_NOT_FOUND_SINGLE_ENTITY;

@Singleton
@Deprecated
public class EntityUtilityService<T1 extends BasicEntity, I1 extends CrudRepository<T1, Long>, T2 extends BasicEntity, I2 extends CrudRepository<T2, Long>>{

    @Inject
    I1 TRepository;

    @Inject
    I2 T2Repository;

    public List<?> returnEntityListFromDtos(Long firstId, Long secondId) throws LinkNotFoundException {

        Optional<T1> firstOptional;
        Optional<T2> secondOptional;
        List<Object> returnList = new ArrayList<>();

        firstOptional = TRepository.findById(firstId);
        if (firstOptional.isPresent()){
            returnList.add(firstOptional.get());
        }else{
            throw new LinkNotFoundException(MESSAGE_NOT_FOUND_SINGLE_ENTITY, T1.ENTITY_NAME);
        }
        secondOptional = T2Repository.findById(secondId);
        if (secondOptional.isPresent()){
            returnList.add(secondOptional.get());
        }else{
            throw new LinkNotFoundException(MESSAGE_NOT_FOUND_SINGLE_ENTITY, T2.ENTITY_NAME);
        }

        return returnList;
    }
}
