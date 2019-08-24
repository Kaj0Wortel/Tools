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
import java.math.BigDecimal;
import tools.MultiTool;


// Java imports
import java.math.BigInteger;
import java.util.Random;


/**
 * Wrapper for the BigInteger class to use it as a toolNumber.
 * 
 * @version 0.1
 * @author Kaj Wortel
 * 
 * @deprecated Old class, and needs to be refactored.
 */
@Deprecated
public class BigIntegerTool
        extends ToolNumberOperationAdapter {
    
    /* -------------------------------------------------------------------------
     * Variables
     * -------------------------------------------------------------------------
     */
    private BigInteger value;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    // BigInteger constructors.
    /**
     * TODO
     * 
     * @param val 
     */
    public BigIntegerTool(byte[] val) {
        value = new BigInteger(val);
    }
    
    /**
     * TODO
     * 
     * @param signum
     * @param magnitude 
     */
    public BigIntegerTool(int signum, byte[] magnitude) {
        value = new BigInteger(signum, magnitude);
    }
    
    /**
     * TODO
     * 
     * @param bitLength
     * @param certainty
     * @param rnd 
     */
    public BigIntegerTool(int bitLength, int certainty, Random rnd) {
        value = new BigInteger(bitLength, certainty, rnd);
    }
    
    /**
     * TODO
     * 
     * @param numBits
     * @param rnd 
     */
    public BigIntegerTool(int numBits, Random rnd) {
        value = new BigInteger(numBits, rnd);
    }
    
    /**
     * TODO
     * 
     * @param val 
     */
    public BigIntegerTool(String val) {
        value = new BigInteger(val);
    }
    /**
     * TODO
     * 
     * @param val
     * @param radix 
     */
    public BigIntegerTool(String val, int radix) {
        value = new BigInteger(val, radix);
    }
    
    // Clone constructors.
    /**
     * TODO 
     * @param val 
     */
    public BigIntegerTool(BigIntegerTool val) {
        this(val.getValue(), true);
    }
    
    /**
     * TODO
     * 
     * @param val 
     */
    public BigIntegerTool(BigInteger val) {
        this(val, true);
    }
    
    /**
     * TODO
     * 
     * @param val
     * @param clone 
     */
    public BigIntegerTool(BigInteger val, boolean clone) {
        if (val == null)
            throw new NullPointerException("value is not allowed to be null!");
        
        value = (clone
                     ? new BigInteger(val.toString())
                     : val);
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from ToolNumber.
     * -------------------------------------------------------------------------
     */
    @Override
    public BigInteger getValue() {
        return value;
    }
    
    @Override
    public BigIntegerTool inverse() {
        return new BigIntegerTool(new BigInteger("1").divide(value));
    }
    
    
    /* -------------------------------------------------------------------------
     * Override from ToolNumberOperationAdapter.
     * -------------------------------------------------------------------------
     */
    /**
     * Does a given operation on two numbers.
     */
    @Override
    protected <T extends ToolNumber> T doOperation
            (Number num1, Number num2, Operation op, boolean update) {
        BigInteger bdt1;
        BigInteger bdt2;
        
        if (num1 instanceof BigIntegerTool) {
            bdt1 = ((BigIntegerTool) num1).getValue();
            
        } else {
            if (num1 instanceof ToolNumber) {
                ToolNumber tNum = (ToolNumber) num1;
                bdt1 = new BigInteger(tNum.getValue().toString());
                
            } else {
                bdt1 = new BigInteger(String.valueOf(num1));
            }
        }
        
        if (num2 instanceof BigDecimalTool) {
            bdt2 = ((BigIntegerTool) num2).getValue();
            
        } else {
            if (num1 instanceof ToolNumber) {
                ToolNumber tNum = (ToolNumber) num2;
                bdt2 = new BigInteger(tNum.getValue().toString());
                
            } else {
                bdt2 = new BigInteger(String.valueOf(num2));
            }
        }
        
        BigInteger bd;
        if (op == Operation.ADD) {
            bd = bdt1.add(bdt2);
            
        } else if (op == Operation.SUB) {
            bd = bdt1.subtract(bdt2);
            
        } else if (op == Operation.MUL) {
            bd = bdt1.multiply(bdt2);
            
        } else if (op == Operation.DIV) {
            bd = bdt1.divide(bdt2);
            
        } else if (op == Operation.MOD) {
            bd = bdt1.mod(bdt2);
            
        } else {
            throw new IllegalArgumentException
                ("Unknown operation found: " + op.toString());
        }
        
        if (update) {
            this.value = bd;
            return (T) this;
            
        } else {
            return (T) new BigIntegerTool(bd);
        }
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
     * Object overrides.
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
                BigDecimalTool bdNum = (BigDecimalTool) obj;
                return new BigDecimal(value).equals(bdNum.getValue());
                
            } else if (obj instanceof BigIntegerTool) {
                BigIntegerTool biNum = (BigIntegerTool) obj;
                return value.equals(biNum.getValue());
                
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