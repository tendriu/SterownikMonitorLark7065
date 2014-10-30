package com.example.app.Sterownik.BinarySerialization;

import android.provider.ContactsContract;

/**
 * Created by nayk on 2014-10-26.
 */
public enum DataType {
     U8(0), U16(1), U32(2), DOUBLE(3), BOOL(4), STRING_ACSI(5), STRING_UNICODE(6), STRUCT(8), S8(9), S16(10), S32(11), DOUBLE_PC(12), ARRAY(128);

    private int value;

    private DataType(int value) {
        this.value = value;
    }

    public static DataType FromValue(int value)
    {
       switch (value)
       {
           case 0:
               return DataType.U8;
           case 1:
               return DataType.U16;
           case 2:
               return DataType.U32;
           case 3:
               return DataType.DOUBLE;
           case 4:
                return DataType.BOOL;
           case 5:
               return DataType.STRING_ACSI;
           case 6:
               return DataType.STRING_UNICODE;
           case 8:
               return DataType.STRUCT;
           case 9:
               return DataType.S8;
           case 10:
               return DataType.S16;
           case 11:
               return DataType.S32;
           case 12:
               return DataType.DOUBLE_PC;
           case 128:
               return DataType.ARRAY;
           default:
               return DataType.U8;
       }
    }

    public int getValue() {
        return value;
    }
}
