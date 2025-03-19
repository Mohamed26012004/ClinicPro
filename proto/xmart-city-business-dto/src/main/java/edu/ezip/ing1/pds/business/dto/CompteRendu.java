
package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "CompteRendu")
public class CompteRendu {
    private  int id_compteRendu;
    private  String typeSymptome;
    private  String descriptionSymptome;


    public CompteRendu() {
    }

    public final CompteRendu build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "id_antecedentMedical","typeSymptome", "descriptionSymptome");
        return this;

    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id_compteRendu),typeSymptome, descriptionSymptome);
    }

    public CompteRendu(String typeSymptome, String descriptionSymptome) {
        this.typeSymptome = typeSymptome;
        this.descriptionSymptome = descriptionSymptome;
    }

    public int getId_compteRendu() {return id_compteRendu;}
    public String getTypeSymptome() {
        return typeSymptome;
    }
    public String getDescriptionSymptome() {
        return descriptionSymptome;
    }

    @JsonProperty("compteRendu_typeSymptome")
    public void setTypeSymptome(String typeSymptome) {this.typeSymptome = typeSymptome;}
    @JsonProperty("compteRendu_descriptionSymptome")
    public void setDescriptionSymptome(String descriptionSymptome) {this.descriptionSymptome = descriptionSymptome;}
    @JsonProperty("compteRendu_id_compteRendu")
    public void setId_compteRendu(int id_antecedentMedical) {
        this.id_compteRendu = id_compteRendu;
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

    @Override
    public String toString() {
        return "CompteRendu{" +
                "id_compteRendu='" + id_compteRendu + '\'' +
                ", typeSymptome='" + typeSymptome + '\'' +
                ", descriptionSymptome='" + descriptionSymptome + '\'' +
                '}';
    }

}

