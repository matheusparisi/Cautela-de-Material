package com.example.admin.cauteladematerial;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static class CautelaDeMaterialTable implements BaseColumns {

        public static final String TABLE_NAME = "cautela_table";
        public static final String _ID = "_id";
        public static final String MATERIAL = "material";
        public static final String MILITAR = "militar";
        public static final String DATA = "data";
        public static final String INFO = "info";
        public static final String ANO = "ano";
        public static final String MES = "mes";
        public static final String DESTINO = "destino";
        public static final String TIPO = "tipo";
        public static final String QUANTIA = "quantia";

    }
}
