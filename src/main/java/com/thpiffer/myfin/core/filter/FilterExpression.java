package com.thpiffer.myfin.core.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa uma expressão de filtro individual (ex: total gt 500).
 * Contém o campo, operador e valor para serem utilizados em buscas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterExpression {
    
    /**
     * Nome do campo (pode incluir notação de ponto para nested: collaborator.id)
     */
    private String field;
    
    /**
     * Operador OData (eq, gt, lt, etc)
     */
    private FilterOperator operator;
    
    /**
     * Valor do filtro (como string, será convertido conforme tipo do campo)
     */
    private String value;
    
    /**
     * Tipo do campo para validação e conversão
     */
    private Class<?> fieldType;
    
    /**
     * Caminho do field nested (ex: collaborator.id retorna [collaborator, id])
     */
    private String[] fieldPath;

    /**
     * Operador de condição lógica para combinar com a próxima expressão (and, or).
     * Null se for a última expressão.
     */
    private String nextCondition;
    
    /**
     * Indica se este é um operador lógico
     */
    public boolean isLogicalOperator() {
        return FilterOperator.isLogicalOperator(operator);
    }
    
    /**
     * Indica se este é um operador de comparação
     */
    public boolean isComparisonOperator() {
        return FilterOperator.isComparisonOperator(operator);
    }
}

