package edu.ezip.ing1.pds.business.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Examens {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("examens")
    private  Set<Examen> examens = new LinkedHashSet<Examen>();


    public final Examens add (final Examen exam) {
        examens.add(exam);
        return this;
    }

    public Set<Examen> getExamens() {
        return examens;
    }

    public void setExamens(Set<Examen> examens) {
        this.examens = examens;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Examens{");
        sb.append("examens=").append(examens);
        sb.append('}');
        return sb.toString();
    }



}
