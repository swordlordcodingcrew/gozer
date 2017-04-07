package com.swordlord.gozer.crosstab;

public class MetaDataInformations
{
    private String[] _columns = null;
    
    public MetaDataInformations(String... columns) {
    	_columns = columns;
    }
    
    public String getColumnName(int i) {
    	return _columns[i - 1];
    }
}
