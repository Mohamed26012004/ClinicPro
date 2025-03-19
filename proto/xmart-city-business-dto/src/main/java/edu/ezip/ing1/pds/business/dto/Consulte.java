package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonRootName(value = "consulte")
public class Consulte {

    private int numeroADELI;
    private int id;

    public  Consulte(){}

    public Consulte(int numeroADELI, int id) {
        this.id = id;
        this.numeroADELI = numeroADELI;
    }
    public final Consulte build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "numeroADELI", "id");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(numeroADELI), String.valueOf(id));
    }
    private void setFieldsFromResulset(final ResultSet resultSet, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        for(final String fieldName : fieldNames ) {
            final Field field = this.getClass().getDeclaredField(fieldName);
            field.set(this, resultSet.getObject(fieldName));
        }
    }
    private final PreparedStatement buildPreparedStatement(PreparedStatement preparedStatement, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        int ix = 0;
        for(final String fieldName : fieldNames ) {
            preparedStatement.setString(++ix, fieldName);
        }
        return preparedStatement;
    }
    public int getNumeroADELI() {
        return numeroADELI;
    }
    @JsonProperty("consulte_numeroADELI")
    public void setNumeroADELI(int numeroADELI) {
        this.numeroADELI = numeroADELI;
    }

    public int getId() {
        return id;
    }
    @JsonProperty("consulte_id")
    public void setId(int id) {
        this.id = id;
    }
}
