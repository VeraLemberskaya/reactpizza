package com.lemberskay.reactpizza.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEntity {
    private long id;

    protected AbstractEntity(){}
    protected  AbstractEntity(long id){
        this.id = id;
    }
}
