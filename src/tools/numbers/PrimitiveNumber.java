/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools.numbers;


// Tools imports
import tools.MultiTool;


// Java imports
import java.lang.reflect.Array;
import tools.data.array.ArrayTools;


/**
 * Representation of the primative Number data types.
 * 
 * @version 0.0
 * @author Kaj Wortel
 * 
 * @deprecated Old class, and needs to be refactored.
 */
@Deprecated
public class PrimitiveNumber<N extends Number>
        extends ToolNumberOperationAdapter
        implements tools.PublicCloneable {
    
    // The data representation.
    protected N value;
    
    // The type of the data representation.
    final public Class<? extends Number> type;
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the given value for {@code value}.
     * 
     * @param num value used to set {@code value}.
     */
    public PrimitiveNumber(N num) {
        for (Class<?> allowClass : PRIMATIVE_NUMBER_CLASSES) {
            if (allowClass.isInstance(num)) {
                type = (Class<Number>) allowClass;
                value = MultiTool.deepClone(num);
                return;
            }
        }
        
        // The provided number was not one of the allowed types,
        // so throw an exception
        throw new IllegalArgumentException
            ("Argument was not a primative data type number.");
    }
    
    public PrimitiveNumber(PrimitiveNumber pNum) {
        value = (N) pNum.getValue();
        type = pNum.type;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Determines the widest class representation for the two classes.
     * 
     * @param c1 class to check.
     * @param c2 class to check.
     * @return the widest class representation of the two classes.
     * @throws NumberFormatException iff
     *     c1 or c2 are not one of the allowed types.
     */
    protected Class<? extends Number> calcWidestType(Class<? extends Number> c1,
                                                  Class<? extends Number> c2) {
        if (c1 == null || c2 == null) {
            throw new NumberFormatException
                ("Expected a primative type, but found: null");
        }
        
        if (c1.equals(c2)) return c1;
        
        for (int i = PRIMATIVE_NUMBER_CLASSES.length - 1; i >= 0; i--) {
            if (PRIMATIVE_NUMBER_CLASSES[i].equals(c1) ||
                    PRIMATIVE_NUMBER_CLASSES[i].equals(c2)) {
                return PRIMATIVE_NUMBER_CLASSES[i];
            }
        }
        
        return null;
    }
    
    /**
     * Generates a {@code PrimitiveNumber} from the provided object.
     * If the object is an array, a new array is created and each entry
     * is recursively generated.
     * 
     * @param obj object to be converted.
     * @return a {@code PrimitiveNumber} (array) representing {@code obj}.
     * @throws IllegalArguementException iff
     *     {@code object} is not a primative number data type class.
     */
    public static Object toPrimNumber(Object obj) {
        if (obj.getClass().isArray()) {
            Object[] result = (Object[]) Array.newInstance
                (PrimitiveNumber.class, ArrayTools.calcDimBalanced(obj));
            
            // Recursivly convert the number
            for (int i = 0; i < Array.getLength(obj); i++) {
                result[i] = toPrimNumber(Array.get(obj, i));
            }
            
            return result;
            
        } else {
            return new PrimitiveNumber((Number) checkPrim(obj));
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from {@code ToolNumberOperationAdapter}.
     * -------------------------------------------------------------------------
     */
    /**
     * Does a given operation on two numbers.
     * 
     * Notes:
     * - None are allowed to be null.
     * - Both values are first converted to a double.
     * 
     * @see ToolNumberOperationAdapter#doOperation(Number, Number,
     *     Operation, boolean) for more info.
     */
    @Override
    protected <T extends ToolNumber> T doOperation(
            Number num1, Number num2, Operation op, boolean update) {
        
        boolean isPrim1 = isPrim(num1);
        boolean isPrim2 = isPrim(num2);
        
        if (num1 instanceof ToolNumber ^ num2 instanceof ToolNumber) {
            if (num1 instanceof PrimitiveNumber) {
                return doOperation(((PrimitiveNumber) num1).getValue(), num2,
                                   op, update);
                
            } else if (num2 instanceof PrimitiveNumber) {
                return doOperation(num1, ((PrimitiveNumber) num2).getValue(),
                                   op, update);
                
            } else {
                throw new NumberFormatException
                    ("Incomparable numbers: " + this.getClass().getName()
                         + " and " + num2.getClass().getName());
            }
        }
        
        if (!isPrim1 || !isPrim2) {
            throw new NumberFormatException
                ("Incomparable numbers: " + this.getClass().getName()
                     + " and " + num2.getClass().getName());
        }
        
        // Here is known that num1 and num2
        // both are prmitive data type classes.
        
        Double dNum1 = num1.doubleValue();
        Double dNum2 = num2.doubleValue();
        Double result;
        
        if (op == Operation.ADD) {
            result = dNum1 + dNum2;
            
        } else if (op == Operation.SUB) {
            result = dNum1 - dNum2;
            
        } else if (op == Operation.MUL) {
            result = dNum1 * dNum2;
            
        } else if (op == Operation.DIV) {
            result = dNum1 / dNum2;
            
        } else if (op == Operation.MOD) {
            result = dNum1 % dNum2;
            
        } else {
            throw new IllegalArgumentException
                ("Unknown operation found: " + op.toString());
        }
        
        Number updateValue;
        Class<? extends Number> cast = update
            ? type
            : calcWidestType(num1.getClass(), num2.getClass());
        
        if (cast.equals(Byte.class)) {
            updateValue = (Byte) result.byteValue();
            
        } else if (cast.equals(Short.class)) {
            updateValue = (Short) result.shortValue();
            
        } else if (cast.equals(Integer.class)) {
            updateValue = (Integer) result.intValue();
            
        } else if (cast.equals(Long.class)) {
            updateValue = (Long) result.longValue();
            
        } else if (cast.equals(Float.class)) {
            updateValue = (Float) result.floatValue();
            
        } else if (cast.equals(Double.class)) {
            updateValue = result;
            
        } else {
            throw new IllegalStateException
                ("Value is not a primative data type number!");
        }
        
        if (update) {
            value = (N) updateValue;
            return (T) this;
            
        } else {
            return (T) new PrimitiveNumber(updateValue);
        }
    }
    
    @Override
    public ToolNumber sqrt() {
        return new PrimitiveNumber<Double>(Math.sqrt(value.doubleValue()));
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from ToolNumber.
     * -------------------------------------------------------------------------
     */
    @Override
    public N getValue() {
        return (N) value;
    }
    
    @Override
    public ToolNumber inverse() {
        return this.divInv(1);
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from Number.
     * -------------------------------------------------------------------------
     */
    @Override
    public byte byteValue() {
        return value.byteValue();
    }
    
    @Override
    public short shortValue() {
        return value.shortValue();
    }
    
    @Override
    public int intValue() {
        return value.intValue();
    }
    
    @Override
    public long longValue() {
        return value.longValue();
    }
    
    @Override
    public float floatValue() {
        return value.floatValue();
    }
    
    @Override
    public double doubleValue() {
        return value.doubleValue();
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from Object.
     * -------------------------------------------------------------------------
     */
    @Override
    public PrimitiveNumber<N> clone() {
        return new PrimitiveNumber<N>(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PrimitiveNumber) {
            return value.equals(((PrimitiveNumber) obj).getValue());
            
        } else if (obj instanceof Number) {
            return obj.equals(value);
            
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    
}