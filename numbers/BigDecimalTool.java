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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;


/* 
 * Wrapper for the BigDecimal class to use it as a toolNumber.
 */
@SuppressWarnings("unchecked")
public class BigDecimalTool
        extends ToolNumberOperationAdapter {
    
    private BigDecimal value;
    
    
    /* -------------------------------------------------------------------------
     * Constructors from BigDecimal
     * -------------------------------------------------------------------------
     */
    public BigDecimalTool(BigInteger val) {
        value = new BigDecimal(val);
    }
    
    public BigDecimalTool(BigInteger unscaledVal, int scale) {
        value = new BigDecimal(unscaledVal, scale);
    }
    
    public BigDecimalTool(BigInteger unscaledVal, int scale, MathContext mc) {
        value = new BigDecimal(unscaledVal, scale, mc);
    }
    
    public BigDecimalTool(BigInteger val, MathContext mc) {
        value = new BigDecimal(val, mc);
    }
    
    public BigDecimalTool(char[] in) {
        value = new BigDecimal(in);
    }
    
    public BigDecimalTool(char[] in, int offset, int len) {
        value = new BigDecimal(in, offset, len);
    }
    
    public BigDecimalTool(char[] in, int offset, int len, MathContext mc) {
        value = new BigDecimal(in, offset, len, mc);
    }
    
    public BigDecimalTool(char[] in, MathContext mc) {
        value = new BigDecimal(in, mc);
    }
    
    public BigDecimalTool(double val) {
        value = new BigDecimal(val);
    }
    
    public BigDecimalTool(double val, MathContext mc) {
        value = new BigDecimal(val, mc);
    }
    
    public BigDecimalTool(int val) {
        value = new BigDecimal(val);
    }
    
    public BigDecimalTool(int val, MathContext mc) {
        value = new BigDecimal(val, mc);
    }
    
    public BigDecimalTool(long val) {
        value = new BigDecimal(val);
    }
    
    public BigDecimalTool(long val, MathContext mc) {
        value = new BigDecimal(val, mc);
    }
    
    public BigDecimalTool(String val) {
        value = new BigDecimal(val);
    }
    
    public BigDecimalTool(String val, MathContext mc) {
        value = new BigDecimal(val, mc);
    }
    
    
    /* -------------------------------------------------------------------------
     * Clone constructors
     * -------------------------------------------------------------------------
     */
    public BigDecimalTool(BigDecimalTool val) {
        this(val.getValue(), true);
    }
    
    public BigDecimalTool(BigIntegerTool val) {
        this(val.getValue());
    }
    
    public BigDecimalTool(BigDecimal val) {
        this(val, true);
    }
    
    public BigDecimalTool(BigDecimal val, boolean clone) {
        if (val == null)
            throw new NullPointerException("value is not allowed to be null!");
        
        value = (clone
                     ? new BigDecimal(val.toString())
                     : val);
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from ToolNumber
     * -------------------------------------------------------------------------
     */
    @Override
    public BigDecimal getValue() {
        return value;
    }
    
    @Override
    public BigDecimalTool inverse() {
        return new BigDecimalTool(new BigDecimal("1").divide(value));
    }
    
    
    /* -------------------------------------------------------------------------
     * Override from ToolNumberOperationAdapter
     * -------------------------------------------------------------------------
     */
    /* 
     * Does a given operation on two numbers.
     */
    @Override
    @SuppressWarnings("unchecked") // <BigDecimalTool> instanceof ToolNumber.
    protected <T extends ToolNumber> T doOperation
            (Number num1, Number num2, Operation op, boolean update) {
        BigDecimal bdt1;
        BigDecimal bdt2;
        
        if (num1 instanceof BigDecimalTool) {
            bdt1 = ((BigDecimalTool) num1).getValue();
            
        } else {
            if (num1 instanceof ToolNumber) {
                ToolNumber tNum = (ToolNumber) num1;
                bdt1 = new BigDecimal(tNum.getValue().toString());
                
            } else {
                bdt1 = new BigDecimal(String.valueOf(num1));
            }
        }
        
        if (num2 instanceof BigDecimalTool) {
            bdt2 = ((BigDecimalTool) num2).getValue();
            
        } else {
            if (num1 instanceof ToolNumber) {
                ToolNumber tNum = (ToolNumber) num2;
                bdt2 = new BigDecimal(tNum.getValue().toString());
                
            } else {
                bdt2 = new BigDecimal(String.valueOf(num2));
            }
        }
        
        BigDecimal bd;
        if (op == Operation.ADD) {
            bd = bdt1.add(bdt2);
            
        } else if (op == Operation.SUB) {
            bd = bdt1.subtract(bdt2);
            
        } else if (op == Operation.MUL) {
            bd = bdt1.multiply(bdt2);
            
        } else if (op == Operation.DIV) {
            bd = bdt1.divide(bdt2);
            
        } else if (op == Operation.MOD) {
            bd = bdt1.divideAndRemainder(bdt2)[1];
            
        } else {
            throw new IllegalArgumentException
                ("Unknown operation found: " + op.toString());
        }
        
        if (update) {
            this.value = bd;
            return (T) this;
            
        } else {
            return (T) new BigDecimalTool(bd);
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from Number
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
     * Other overrides
     * -------------------------------------------------------------------------
     */
    @Override
    public ToolNumber clone() {
        return new BigDecimalTool(this);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Number)) return false;
        
        if (obj instanceof ToolNumber) {
            if (obj instanceof BigDecimalTool) {
                BigDecimalTool bNum = (BigDecimalTool) obj;
                return value.equals(bNum.value);
                
            } else if (obj instanceof BigIntegerTool) {
                BigIntegerTool bNum = (BigIntegerTool) obj;
                return value.equals(new BigDecimalTool(bNum.getValue()));
                
            } else {
                ToolNumber tn = (ToolNumber) obj;
                return tn.equals(value.doubleValue());
            }
            
        } else {
            return value.equals(obj);
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(value);
    }
    
}