package com.thpiffer.myfin.core.filter;

import com.thpiffer.myfin.core.exception.InvalidFilterException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser para interpretação de strings de filtro OData.
 * 
 * Exemplos de filtros suportados:
 * - total gt 500
 * - total gt 500 and collaborator.id eq '49293'
 * - code eq 123 or total le 100.50
 * - name startswith 'John'
 * - description contains 'test'
 */
@Slf4j
public class ODataFilterParser {

    private static final String LOGICAL_OPERATOR_PATTERN = "\\s+(and|or)\\s+";
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
        "^\\s*([\\w.]+)\\s+(eq|ne|gt|ge|lt|le|startswith|endswith|contains)\\s+(.+?)(?:\\s+(?:and|or)\\s+|$)"
    );

    private final Class<?> entityClass;
    private final Map<String, Field> fieldMap;

    public ODataFilterParser(Class<?> entityClass) {
        this.entityClass = entityClass;
        this.fieldMap = buildFieldMap(entityClass);
    }

    /**
     * Realiza o parsing da string de filtro OData.
     */
    public ODataFilter parse(String filterString) {
        if (filterString == null || filterString.trim().isEmpty()) {
            return ODataFilter.builder()
                    .expressions(new ArrayList<>())
                    .entityClass(entityClass)
                    .filterString(filterString)
                    .build();
        }

        try {
            List<FilterExpression> expressions = parseExpressions(filterString);
            
            return ODataFilter.builder()
                    .expressions(expressions)
                    .entityClass(entityClass)
                    .filterString(filterString)
                    .build();
        } catch (InvalidFilterException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidFilterException("Erro ao interpretar filtro: " + e.getMessage(), e);
        }
    }

    /**
     * Realiza o parsing das expressões.
     */
    private List<FilterExpression> parseExpressions(String filterString) {
        List<FilterExpression> expressions = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        // Use Pattern and Matcher to extract both expressions AND operators
        Pattern logicalOpPattern = Pattern.compile(LOGICAL_OPERATOR_PATTERN);
        Matcher opMatcher = logicalOpPattern.matcher(filterString);

        while (opMatcher.find()) {
            operators.add(opMatcher.group(1));
        }

        String[] parts = filterString.split(LOGICAL_OPERATOR_PATTERN);

        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                FilterExpression expr = parseExpression(part);
                expr.setNextCondition(!operators.isEmpty() ? operators.remove(0) : null);
                expressions.add(expr);
            }
        }

        if (expressions.isEmpty()) {
            throw new InvalidFilterException("Nenhuma expressão de filtro válida foi encontrada");
        }

        return expressions;
    }

    /**
     * Realiza o parsing de uma expressão individual.
     */
    private FilterExpression parseExpression(String expression) {
        expression = expression.trim();
        
        // Tenta encontrar um operador de comparação
        String[] operators = {"startswith", "endswith", "contains", "eq", "ne", "ge", "le", "gt", "lt"};
        
        for (String op : operators) {
            Pattern pattern = Pattern.compile("^([\\w.]+)\\s+" + op + "\\s+(.+)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(expression);
            
            if (matcher.find()) {
                String fieldName = matcher.group(1);
                String valueStr = matcher.group(2).trim();
                
                // Valida o campo
                Field field = validateAndGetField(fieldName);
                
                // Remove aspas se presentes
                valueStr = removeQuotes(valueStr);
                
                // Valida e converte o valor
                Object convertedValue = validateAndConvertValue(fieldName, field, valueStr, op);
                
                return FilterExpression.builder()
                        .field(fieldName)
                        .operator(FilterOperator.fromSymbol(op))
                        .value(convertedValue.toString())
                        .fieldType(field.getType())
                        .fieldPath(fieldName.split("\\."))
                        .build();
            }
        }
        
        throw new InvalidFilterException("Expressão de filtro inválida: " + expression);
    }

    /**
     * Valida e retorna o Field correspondente ao nome do campo.
     */
    private Field validateAndGetField(String fieldName) {
        String[] fieldPath = fieldName.split("\\.");
        
        if (fieldPath.length == 1) {
            // Campo simples
            Field field = fieldMap.get(fieldName);
            if (field == null) {
                throw new InvalidFilterException(
                    String.format("Campo '%s' não encontrado na entidade '%s'", 
                        fieldName, entityClass.getSimpleName())
                );
            }
            return field;
        } else {
            // Campo nested (ex: collaborator.id)
            Field currentField = fieldMap.get(fieldPath[0]);
            if (currentField == null) {
                throw new InvalidFilterException(
                    String.format("Campo '%s' não encontrado na entidade '%s'", 
                        fieldPath[0], entityClass.getSimpleName())
                );
            }
            
            Class<?> currentClass = currentField.getType();
            
            for (int i = 1; i < fieldPath.length; i++) {
                try {
                    currentField = currentClass.getDeclaredField(fieldPath[i]);
                    currentClass = currentField.getType();
                } catch (NoSuchFieldException e) {
                    throw new InvalidFilterException(
                        String.format("Campo '%s' não encontrado na classe '%s'", 
                            fieldPath[i], currentClass.getSimpleName())
                    );
                }
            }
            
            return currentField;
        }
    }

    /**
     * Valida e converte o valor conforme o tipo do campo.
     */
    private Object validateAndConvertValue(String fieldName, Field field, String valueStr, String operator) {
        Class<?> fieldType = field.getType();
        
        try {
            // Para operadores de string, permite qualquer valor
            if (isStringOperator(operator)) {
                if (!isStringType(fieldType)) {
                    throw new InvalidFilterException(
                        String.format("Operador '%s' só pode ser usado em campos do tipo String. Campo '%s' é do tipo '%s'",
                            operator, fieldName, fieldType.getSimpleName())
                    );
                }
                return valueStr;
            }
            
            // Para outros operadores, valida a conversão de tipo
            return convertValue(valueStr, fieldType, fieldName);
        } catch (InvalidFilterException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidFilterException(
                String.format("Não foi possível converter o valor '%s' para o tipo '%s' do campo '%s': %s",
                    valueStr, fieldType.getSimpleName(), fieldName, e.getMessage()),
                e
            );
        }
    }

    /**
     * Converte o valor string para o tipo do campo.
     */
    private Object convertValue(String valueStr, Class<?> targetType, String fieldName) {
        if (valueStr == null || valueStr.isEmpty()) {
            throw new InvalidFilterException(
                String.format("Valor vazio para o campo '%s'", fieldName)
            );
        }
        
        try {
            // Tipos primitivos e wrappers
            if (targetType == String.class) {
                return valueStr;
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(valueStr);
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(valueStr);
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(valueStr);
            } else if (targetType == Float.class || targetType == float.class) {
                return Float.parseFloat(valueStr);
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.parseBoolean(valueStr);
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(valueStr);
            } else if (targetType == LocalDate.class) {
                return LocalDate.parse(valueStr);
            } else if (targetType == LocalDateTime.class) {
                return LocalDateTime.parse(valueStr);
            } else if (targetType == UUID.class) {
                return UUID.fromString(valueStr);
            } else if (targetType == Date.class) {
                return new Date(Long.parseLong(valueStr));
            } else if (targetType.isEnum()) {
                return Enum.valueOf((Class<Enum>) targetType, valueStr.toUpperCase());
            } else {
                throw new InvalidFilterException(
                    String.format("Tipo '%s' não é suportado para filtros", targetType.getSimpleName())
                );
            }
        } catch (NumberFormatException e) {
            throw new InvalidFilterException(
                String.format("'%s' não é um valor válido para o tipo %s", valueStr, targetType.getSimpleName()),
                e
            );
        } catch (Exception e) {
            throw new InvalidFilterException(
                String.format("Erro ao converter '%s' para %s: %s", valueStr, targetType.getSimpleName(), e.getMessage()),
                e
            );
        }
    }

    /**
     * Remove aspas de um valor se presentes.
     */
    private String removeQuotes(String value) {
        if (value.startsWith("'") && value.endsWith("'")) {
            return value.substring(1, value.length() - 1);
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Verifica se o operador é um operador de string.
     */
    private boolean isStringOperator(String operator) {
        return "startswith".equalsIgnoreCase(operator) || 
               "endswith".equalsIgnoreCase(operator) || 
               "contains".equalsIgnoreCase(operator);
    }

    /**
     * Verifica se o tipo é compatível com operadores de string.
     */
    private boolean isStringType(Class<?> type) {
        return type == String.class;
    }

    /**
     * Constrói um mapa de todos os campos da classe.
     */
    private Map<String, Field> buildFieldMap(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        
        for (Field field : clazz.getDeclaredFields()) {
            map.put(field.getName(), field);
        }
        
        // Inclui campos da superclasse se existir
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && !superClass.equals(Object.class)) {
            for (Field field : superClass.getDeclaredFields()) {
                if (!map.containsKey(field.getName())) {
                    map.put(field.getName(), field);
                }
            }
            superClass = superClass.getSuperclass();
        }
        
        return map;
    }

}
