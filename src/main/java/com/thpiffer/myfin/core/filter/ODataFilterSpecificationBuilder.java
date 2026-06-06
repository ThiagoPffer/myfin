package com.thpiffer.myfin.core.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Utilidade para converter ODataFilter em Spring Data JPA Specifications.
 * Permite usar os filtros interpretados diretamente em repositórios JPA.
 */
public class ODataFilterSpecificationBuilder {

    /**
     * Constrói uma Specification a partir de um ODataFilter.
     * 
     * @param oDataFilter Filtro OData interpretado
     * @param <T> Tipo da entidade
     * @return Specification que pode ser usada no repositório
     */
    public static <T> Specification<T> buildSpecification(ODataFilter oDataFilter) {
        if (oDataFilter == null || oDataFilter.isEmpty()) {
            return null;
        }

        List<FilterExpression> expressions = oDataFilter.getExpressions();
        Specification<T> spec = buildSingleExpressionSpecification(expressions.get(0));

        if (expressions.size() == 1) {
            return spec;
        }

        for (int i = 1; i < expressions.size(); i++) {
            if (Objects.equals(expressions.get(i).getNextCondition(), "AND")) {
                spec = spec.and(buildSingleExpressionSpecification(expressions.get(i)));
            } else {
                spec = spec.or(buildSingleExpressionSpecification(expressions.get(i)));
            }
        }
        
        return spec;
    }

    /**
     * Constrói uma Specification para uma expressão de filtro individual.
     */
    private static <T> Specification<T> buildSingleExpressionSpecification(FilterExpression expression) {
        FilterOperator operator = expression.getOperator();
        String value = expression.getValue();

        return (root, query, cb) -> {
            // Resolve o caminho do campo (suporta nested)
            jakarta.persistence.criteria.Path<?> path = root.get(expression.getFieldPath()[0]);
            
            for (int i = 1; i < expression.getFieldPath().length; i++) {
                path = path.get(expression.getFieldPath()[i]);
            }

            // Aplica o operador apropriado
            return switch (operator) {
                case EQ -> cb.equal(path, convertValue(value, expression.getFieldType()));
                case NE -> cb.notEqual(path, convertValue(value, expression.getFieldType()));
                case GT -> cb.greaterThan((jakarta.persistence.criteria.Expression<? extends Comparable>) path,
                        (Comparable) convertValue(value, expression.getFieldType()));
                case GE -> cb.greaterThanOrEqualTo((jakarta.persistence.criteria.Expression<? extends Comparable>) path,
                        (Comparable) convertValue(value, expression.getFieldType()));
                case LT -> cb.lessThan((jakarta.persistence.criteria.Expression<? extends Comparable>) path,
                        (Comparable) convertValue(value, expression.getFieldType()));
                case LE -> cb.lessThanOrEqualTo((jakarta.persistence.criteria.Expression<? extends Comparable>) path,
                        (Comparable) convertValue(value, expression.getFieldType()));
                case CONTAINS -> cb.like(cb.lower((jakarta.persistence.criteria.Expression<String>) path),
                        "%" + value.toLowerCase() + "%");
                case STARTSWITH -> cb.like(cb.lower((jakarta.persistence.criteria.Expression<String>) path),
                        value.toLowerCase() + "%");
                case ENDSWITH -> cb.like(cb.lower((jakarta.persistence.criteria.Expression<String>) path),
                        "%" + value.toLowerCase());
                default -> throw new IllegalArgumentException("Operador não suportado: " + operator);
            };
        };
    }

    /**
     * Converte um valor string para o tipo especificado.
     */
    private static Object convertValue(String value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        return switch (targetType.getName()) {
            case "java.lang.String" -> value;
            case "java.util.UUID" -> UUID.fromString(value);
            case "java.lang.Integer", "int" -> Integer.parseInt(value);
            case "java.lang.Long", "long" -> Long.parseLong(value);
            case "java.lang.Double", "double" -> Double.parseDouble(value);
            case "java.lang.Float", "float" -> Float.parseFloat(value);
            case "java.lang.Boolean", "boolean" -> Boolean.parseBoolean(value);
            case "java.math.BigDecimal" -> new java.math.BigDecimal(value);
            default -> value; // Retorna como string se o tipo não for reconhecido
        };
    }
}

