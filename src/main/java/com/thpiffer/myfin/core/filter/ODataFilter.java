package com.thpiffer.myfin.core.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Encapsula a estrutura de filtros OData interpretada.
 * Contém a árvore de expressões de filtro que podem ser utilizadas
 * em repositórios para construir queries dinâmicas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ODataFilter {
    
    /**
     * Lista de expressões de filtro
     */
    private List<FilterExpression> expressions;
    
    /**
     * Classe de entidade para a qual o filtro foi criado
     */
    private Class<?> entityClass;
    
    /**
     * String original do filtro
     */
    private String filterString;
    
    /**
     * Verifica se o filtro está vazio (sem expressões)
     */
    public boolean isEmpty() {
        return expressions == null || expressions.isEmpty();
    }
    
    /**
     * Retorna o número de expressões
     */
    public int getExpressionCount() {
        return expressions == null ? 0 : expressions.size();
    }
}

