package com.UU.UUPokedexSeptiembre2025Service.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class ResultJPA {
    
    public boolean correct;
    public String errorMessage;
    public Object object;
    public List<Object> objects;
    public Exception ex;
    
    @JsonIgnore
    public int status;
}
