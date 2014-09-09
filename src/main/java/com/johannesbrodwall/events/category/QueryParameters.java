package com.johannesbrodwall.events.category;

import java.util.List;
import java.util.Map;

import lombok.Getter;

public class QueryParameters {
    @Getter
    private List<String> fetchRelations;
    @Getter
    private List<String> includedFields;
    @Getter
    private List<String> sortFields;
    @Getter
    private Map<String, String> filters;
    @Getter
    private int fetchFrom;
    @Getter
    private int fetchSize;

}
