package com.thpiffer.myfin.core.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum com os operadores suportados conforme padrão OData.
 */
public enum FilterOperator {
    // Comparison operators
    EQ("eq", "equals"),
    NE("ne", "not equals"),
    GT("gt", "greater than"),
    GE("ge", "greater than or equal"),
    LT("lt", "less than"),
    LE("le", "less than or equal"),
    
    // String operators
    STARTSWITH("startswith", "starts with"),
    ENDSWITH("endswith", "ends with"),
    CONTAINS("contains", "contains"),
    
    // Logical operators
    AND("and", "logical and"),
    OR("or", "logical or");

    private final String symbol;
    private final String description;

    private static final Map<String, FilterOperator> SYMBOL_MAP = new HashMap<>();

    static {
        for (FilterOperator op : FilterOperator.values()) {
            SYMBOL_MAP.put(op.symbol, op);
        }
    }

    FilterOperator(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public static FilterOperator fromSymbol(String symbol) {
        FilterOperator op = SYMBOL_MAP.get(symbol);
        if (op == null) {
            throw new IllegalArgumentException("Operador inválido: " + symbol);
        }
        return op;
    }

    public static boolean isLogicalOperator(FilterOperator op) {
        return op == AND || op == OR;
    }

    public static boolean isComparisonOperator(FilterOperator op) {
        return !isLogicalOperator(op);
    }
}

