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


/**
 * Class representing a complex number.
 * 
 * @apiNote
 * Non-complex numbers are treated as real numbers.
 * 
 * @version 0.1
 * @author Kaj Wortel
 */
public class ComplexNumber<N extends Number>
        extends ToolNumberAdapter<ComplexNumber> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The real part of the number. */
    protected ToolNumber real;
    /** The complex part of the number. */
    protected ToolNumber comp;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    public ComplexNumber(N r) {
        real = new PrimitiveNumber<N>(r);
        comp = real.mul((short) 0);
    }
    
    /**
     * Creates a complex number from a real and complex part.
     * 
     * @param real The real value of the number.
     * @param comp The complex value of the number.
     */
    public ComplexNumber(N real, N comp) {
        this.real = new PrimitiveNumber<N>(real);
        this.comp = new PrimitiveNumber<N>(comp);
    }
    
    /**
     * Creates a new complex number from the given complex number.
     * The number is cloned by default.
     * 
     * @param comp The complex value of the number.
     */
    public ComplexNumber(ToolNumber comp) {
        this(comp.sub(comp), comp, true);
    }
    
    /**
     * Creates a new complex number from two tool numbers.
     * The numbers are cloned by default.
     * 
     * @param real The real value of the number.
     * @param comp The complex value of the number.
     */
    public ComplexNumber(ToolNumber real, ToolNumber comp) {
        this(real, comp, true);
    }
    
    /**
     * Creates a new complex number from two tool numbers.
     * 
     * @param real The real value of the number.
     * @param comp The complex value of the number.
     * @param clone Whether the tool number should be cloned.
     */
    public ComplexNumber(ToolNumber real, ToolNumber comp, boolean clone) {
        if (clone) {
            this.real = real.clone();
            this.comp = comp.clone();
            
        } else {
            this.real = real;
            this.comp = comp;
        }
    }
    
    /**
     * Creates a new complex number from a complex number.
     * 
     * @param cNum The complex number to be cloned.
     */
    public ComplexNumber(ComplexNumber<N> cNum) {
        real = cNum.real.clone();
        comp = cNum.comp.clone();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return the real part of the number.
     */
    public N getReal() {
        return (N) real.getValue();
    }
    
    /**
     * @return the complex part of the number.
     */
    public N getComp() {
        return (N) comp.getValue();
    }
    
    /**
     * @return the conjugate of this number.
     */
    public ComplexNumber<N> calcConjugate() {
        return new ComplexNumber<N>(real.clone(), comp.mul(-1));
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from ToolNumber.
     * -------------------------------------------------------------------------
     */
    @Override
    public N getValue() {
        return (N) real.getValue();
    }
    
    @Override
    public ComplexNumber<N> inverse() {
        /* (a+bi)^-1
         *     = 1 / (a+bi)
         *     = (a - bi) / (a^2 + b^2)
         *     = a / c - b / c where c = a^2 + b^2
         */
        ToolNumber c = real.mul(real).addi(comp.mul(comp));
        return new ComplexNumber(c.divInv(real), c.divInvi(comp));
    }
    
    @Override
    public ToolNumber sqrt() {
        // todo
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from ToolNumberDecorator.
     * -------------------------------------------------------------------------
     */
    @Override
    protected <T extends ComplexNumber> T addTool(ToolNumber tn1,
            ToolNumber tn2, T result) {
        ToolNumber realRes;
        ToolNumber compRes;
        
        if (tn1 instanceof ComplexNumber) {
            ComplexNumber cn1 = (ComplexNumber) tn1;
            
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Both are complex numbers.
                realRes = cn1.real.add(cn2.real);
                compRes = cn1.comp.add(cn2.comp);
                
            } else {
                // Only tn1 is a complex number.
                realRes = cn1.real.add(tn2);
                compRes = cn1.comp.clone();
            }
            
        } else {
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Only tn2 is a complex number.
                realRes = cn2.real.addInv(tn1);
                compRes = cn2.comp.clone();
                
            } else {
                // Neither is a complex number.
                throw new NumberFormatException
                    ("Neither of the given numbers is a complex number.");
            }
        }
        
        if (result == null) {
            result = (T) new ComplexNumber(realRes, compRes, false);
            
        } else {
            result.real = realRes;
            result.comp = compRes;
        }
        
        return result;
    }
    
    @Override
    protected <T extends ComplexNumber> T subTool(ToolNumber tn1,
            ToolNumber tn2, T result) {
        ToolNumber realRes;
        ToolNumber compRes;
        
        if (tn1 instanceof ComplexNumber) {
            ComplexNumber cn1 = (ComplexNumber) tn1;
            
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Both are complex numbers.
                realRes = cn1.real.sub(cn2.real);
                compRes = cn1.comp.sub(cn2.comp);
                
            } else {
                // Only tn1 is a complex number.
                realRes = cn1.real.sub(tn2);
                compRes = cn1.comp.clone();
            }
            
        } else {
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Only tn2 is a complex number.
                realRes = cn2.real.subInv(tn1);
                compRes = cn2.comp.clone();
                
            } else {
                // Neither is a complex number.
                throw new NumberFormatException
                    ("Neither of the given numbers is a complex number.");
            }
        }
        
        if (result == null) {
            result = (T) new ComplexNumber(realRes, compRes, false);
            
        } else {
            result.real = realRes;
            result.comp = compRes;
        }
        
        return result;
    }
    
    @Override
    protected <T extends ComplexNumber> T mulTool(ToolNumber tn1,
            ToolNumber tn2, T result) {
        ToolNumber realRes;
        ToolNumber compRes;
        
        if (tn1 instanceof ComplexNumber) {
            ComplexNumber cn1 = (ComplexNumber) tn1;
            
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Both are complex numbers.
                // Uses (a+bi)*(c+di) = (ac-bd) + (ad+bc)i.
                realRes = cn1.real.mul(cn2.real)
                    .subi(cn1.comp.mul(cn2.comp));
                compRes = cn1.real.mul(cn2.comp)
                    .addi(cn1.comp.mul(cn2.real));
                
            } else {
                // Only tn1 is a complex number.
                realRes = cn1.real.mul(tn2);
                compRes = cn1.comp.mul(tn2);
            }
            
        } else {
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Only tn2 is a complex number.
                realRes = cn2.real.mulInv(tn1);
                compRes = cn2.comp.mulInv(tn1);
                
            } else {
                // Neither is a complex number.
                throw new NumberFormatException
                    ("Neither of the given numbers is a complex number.");
            }
        }
        
        if (result == null) {
            result = (T) new ComplexNumber(realRes, compRes, false);
            
        } else {
            result.real = realRes;
            result.comp = compRes;
        }
        
        return result;
    }
    
    @Override
    protected <T extends ComplexNumber> T divTool(ToolNumber tn1,
            ToolNumber tn2, T result) {
        ToolNumber realRes;
        ToolNumber compRes;
        
        if (tn1 instanceof ComplexNumber) {
            ComplexNumber cn1 = (ComplexNumber) tn1;
            
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Both are complex numbers.
                // Uses (a+bi)/(c+di) = (ac+bd)/(cc+dd) + (bc-ad)i/(cc+dd).
                ToolNumber divisor = cn2.real.mul(cn2.real)
                    .addi(cn2.comp.mul(cn2.comp));
                realRes = cn1.real.mul(cn2.real)
                    .addi(cn1.comp.mul(cn2.comp)).divi(divisor);
                compRes = cn1.comp.mul(cn2.real)
                    .subi(cn1.real.mul(cn2.comp)).divi(divisor);
                
            } else {
                // Only tn1 is a complex number.
                realRes = cn1.real.div(tn2);
                compRes = cn1.comp.div(tn2);
            }
            
        } else {
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Only tn2 is a complex number.
                realRes = cn2.real.divInv(tn1);
                compRes = cn2.comp.divInv(tn1);
                
            } else {
                // Neither is a complex number.
                throw new NumberFormatException
                    ("Neither of the given numbers is a complex number.");
            }
        }
        
        if (result == null) {
            result = (T) new ComplexNumber(realRes, compRes, false);
            
        } else {
            result.real = realRes;
            result.comp = compRes;
        }
        
        return result;
    }
    
    @Override
    protected <T extends ComplexNumber> T modTool(ToolNumber tn1,
            ToolNumber tn2, T result) {
        ToolNumber realRes;
        ToolNumber compRes;
        
        if (tn1 instanceof ComplexNumber) {
            ComplexNumber cn1 = (ComplexNumber) tn1;
            
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Both are complex numbers.
                realRes = cn1.real.mod(cn2.real);
                compRes = cn1.comp.mod(cn2.comp);
                
            } else {
                // Only tn1 is a complex number.
                realRes = cn1.real.mod(tn2);
                compRes = cn1.comp.mod(tn2);
            }
            
        } else {
            if (tn2 instanceof ComplexNumber) {
                ComplexNumber cn2 = (ComplexNumber) tn2;
                
                // Only tn2 is a complex number.
                realRes = cn2.real.modInv(tn1);
                compRes = cn2.comp.modInv(tn1);
                
            } else {
                // Neither is a complex number.
                throw new NumberFormatException
                    ("Neither of the given numbers is a complex number.");
            }
        }
        
        if (result == null) {
            result = (T) new ComplexNumber(realRes, compRes, false);
            
        } else {
            result.real = realRes;
            result.comp = compRes;
        }
        
        return result;
    }
    
    @Override
    public void update(ComplexNumber cn) {
        real = cn.real;
        comp = cn.comp;
    }
    
    /* -------------------------------------------------------------------------
     * Overrides from Number.
     * -------------------------------------------------------------------------
     */
    @Override
    public byte byteValue() {
        return real.byteValue();
    }
    
    @Override
    public short shortValue() {
        return real.shortValue();
    }
    
    @Override
    public int intValue() {
        return real.intValue();
    }
    
    @Override
    public long longValue() {
        return real.longValue();
    }
    
    @Override
    public float floatValue() {
        return real.floatValue();
    }
    
    @Override
    public double doubleValue() {
        return real.doubleValue();
    }
    
    
    /* -------------------------------------------------------------------------
     * Other overrides.
     * -------------------------------------------------------------------------
     */
    @Override
    public String toString() {
        return "(" + real.toString() + " + " + comp.toString() + " i)";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Number)) return false;
        
        if (obj instanceof ComplexNumber) {
            ComplexNumber cNum = (ComplexNumber) obj;
            return real.equals(cNum.real) && comp.equals(cNum.comp);
            
        } else {
            Number num = (Number) obj;
            return comp.doubleValue() == 0.0 && real.equals(num);
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {real, comp});
    }
    
    @Override
    public ComplexNumber clone() {
        return new ComplexNumber<N>(this);
    }
    
    
    // tmp
    public static void main(String[] args) {
        ComplexNumber<Double>  num1 = new ComplexNumber<>(5.0, 2.0);
        ComplexNumber<Integer> num2 = new ComplexNumber<>(5, 2);
        System.out.println(num1);
        System.out.println(num1.add(num2));
        System.out.println(num1);
        System.out.println(num1.addi(num2));
        System.out.println(num1);
        
        System.out.println("-----------------");
        System.out.println(num1.mul(num2));
    }
    
    
}