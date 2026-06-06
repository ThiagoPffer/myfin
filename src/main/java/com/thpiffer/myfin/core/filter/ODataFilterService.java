package com.thpiffer.myfin.core.filter;

import com.thpiffer.myfin.core.exception.InvalidFilterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por orquestrar o parsing de filtros OData.
 * Fornece uma interface simples para os controllers utilizarem.
 */
@Slf4j
@Service
public class ODataFilterService {

    /**
     * Realiza o parsing de um filtro OData para a classe de entidade especificada.
     * 
     * @param filterString String contendo o filtro (ex: "total gt 500 and collaborator.id eq '49293'")
     * @param entityClass Classe da entidade para validação
     * @return ODataFilter contendo as expressões interpretadas
     * @throws InvalidFilterException Se o filtro for inválido
     */
    public ODataFilter parseFilter(String filterString, Class<?> entityClass) {
        log.debug("Parsing filter '{}' for entity class '{}'", filterString, entityClass.getSimpleName());
        
        if (filterString == null || filterString.trim().isEmpty()) {
            log.debug("Empty filter string provided");
            return ODataFilter.builder()
                    .expressions(new java.util.ArrayList<>())
                    .entityClass(entityClass)
                    .filterString(filterString)
                    .build();
        }
        
        ODataFilterParser parser = new ODataFilterParser(entityClass);
        ODataFilter filter = parser.parse(filterString);
        
        log.info("Filter parsed successfully. Expressions count: {}", filter.getExpressionCount());
        return filter;
    }

    /**
     * Valida se um filtro é válido sem efetivamente processá-lo.
     * Útil para validação prévia.
     * 
     * @param filterString String contendo o filtro
     * @param entityClass Classe da entidade para validação
     * @return true se o filtro é válido, false caso contrário
     */
    public boolean isValidFilter(String filterString, Class<?> entityClass) {
        try {
            parseFilter(filterString, entityClass);
            return true;
        } catch (InvalidFilterException e) {
            log.warn("Invalid filter: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrai o valor de um campo em um objeto, suportando notação de ponto.
     * Exemplo: "collaborator.id" retornará o ID do colaborador do objeto.
     * 
     * @param obj Objeto de onde extrair o valor
     * @param fieldPath Caminho do campo (ex: "collaborator.id")
     * @return Valor do campo ou null
     */
    public Object extractFieldValue(Object obj, String fieldPath) {
        if (obj == null || fieldPath == null || fieldPath.isEmpty()) {
            return null;
        }
        
        String[] parts = fieldPath.split("\\.");
        Object current = obj;
        
        for (String part : parts) {
            if (current == null) {
                return null;
            }
            
            try {
                java.lang.reflect.Field field = current.getClass().getDeclaredField(part);
                field.setAccessible(true);
                current = field.get(current);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("Unable to extract field '{}' from object of type '{}'", 
                    part, current.getClass().getSimpleName());
                return null;
            }
        }
        
        return current;
    }
}

