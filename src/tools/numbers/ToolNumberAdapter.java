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
import tools.StoreException;


/**
 * This decorator converts each input number to a default {@code ToolNumber}
 * using the convert function.
 * 
 * Note that this must be an instance of C.
 * 
 * @version 0.0
 * @author Kaj Wortel
 * 
 * @deprecated Old class, and needs to be refactored.
 */
@Deprecated
public abstract class ToolNumberAdapter<C extends ToolNumber>
        extends ToolNumber {
    
    /**
     * Converts the given number to a ToolNumber.
     * Should be overridden if more specific ToolNumbers are needed.
     * 
     * @param num the number to be converted.
     * @return the converted number {@code num}.
     */
    protected ToolNumber convert(Number num) {
        if (num == null) throw new NullPointerException
                ("Expected a Number, but found null!");
        if (num instanceof ToolNumber) return (ToolNumber) num;
        
        return (PrimitiveNumber) PrimitiveNumber.toPrimNumber(num);
    }
    
    /**
     * Updates this to the given update.
     * 
     * @param update the value to be updated to.
     */
    protected void updateThis(ToolNumber update) {
        if (this.getClass().isInstance(update)) {
            update((C) update);
            
        } else {
            throw new StoreException
                ("Tried to store " + update.getClass().getName() + " in a "
                     + this.getClass().getName());
        }
    }
    
    protected abstract void update(C update);
    
    
    /**-------------------------------------------------------------------------
     * Addition.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T add(Number num) {
        return (T) addTool(this, convert(num), null);
    }
    
    @Override
    public <T extends ToolNumber> T addi(Number num) {
        updateThis(addTool(this, convert(num), (C) this));
        return (T) this;
    }
    
    @Override
    public <T extends ToolNumber> T addInv(Number num) {
        return (T) addTool(convert(num), this, null);
    }
    
    @Override
    public <T extends ToolNumber> T addInvi(Number num) {
        updateThis(addTool(convert(num), this, (C) this));
        return (T) this;
    }
    
    /**
     * Calculate the addition of tn1 and tn2.
     * Modifies and returns {@code result}.
     * 
     * @param tn1
     * @param tn2
     * @result will contain the result of the operation. If null, a new
     *     ToolNumber should be created.
     * @return if {@code result == null}, {@code tn1 + tn2}.
     *     Otherwise {@code result}, where {@code result == tn1 + tn2}.
     */
    protected abstract <V extends C> V addTool(ToolNumber tn1, ToolNumber tn2,
                                               V result);
    
    
    /**-------------------------------------------------------------------------
     * Subtraction.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T sub(Number num) {
        return (T) subTool(this, convert(num), null);
    }
    
    @Override
    public <T extends ToolNumber> T subi(Number num) {
        updateThis(subTool(this, convert(num), (C) this));
        return (T) this;
    }
    
    @Override
    public <T extends ToolNumber> T subInv(Number num) {
        return (T) subTool(convert(num), this, null);
    }
    
    @Override
    public <T extends ToolNumber> T subInvi(Number num) {
        updateThis(subTool(convert(num), this, (C) this));
        return (T) this;
    }
    
    /* 
     * Calculate the subtraction of tn2 from tn1.
     * Modifies and returns {@code result}.
     * 
     * @param tn1
     * @param tn2
     * @result will contain the result of the operation. If null, a new
     *     ToolNumber should be created.
     * @return if {@code result == null}, {@code tn1 - tn2}.
     *     Otherwise {@code result}, where {@code result == tn1 - tn2}.
     */
    protected abstract <V extends C> V subTool(ToolNumber tn1, ToolNumber tn2,
                                               V result);
    
    
    /**-------------------------------------------------------------------------
     * Multiplication.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T mul(Number num) {
        return (T) mulTool(this, convert(num), null);
    }
    
    @Override
    public <T extends ToolNumber> T muli(Number num) {
        updateThis(mulTool(this, convert(num), (C) this));
        return (T) this;
    }
    
    @Override
    public <T extends ToolNumber> T mulInv(Number num) {
        return (T) mulTool(convert(num), this, null);
    }
    
    @Override
    public <T extends ToolNumber> T mulInvi(Number num) {
        updateThis(mulTool(convert(num), this, (C) this));
        return (T) this;
    }
    
    /**
     * Calculate the multiplication of tn1 and tn2.
     * Modifies and returns {@code result}.
     * 
     * @param tn1
     * @param tn2
     * @result will contain the result of the operation. If null, a new
     *     ToolNumber should be created.
     * @return if {@code result == null}, {@code tn1 * tn2}.
     *     Otherwise {@code result}, where {@code result == tn1 * tn2}.
     */
    protected abstract <V extends C> V mulTool(ToolNumber tn1, ToolNumber tn2,
                                               V result);
    
    
    /**-------------------------------------------------------------------------
     * Division.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T div(Number num) {
        return (T) divTool(this, convert(num), null);
    }
    
    @Override
    public <T extends ToolNumber> T divi(Number num) {
        updateThis(divTool(this, convert(num), (C) this));
        return (T) this;
    }
    
    @Override
    public <T extends ToolNumber> T divInv(Number num) {
        return (T) divTool(convert(num), this, null);
    }
    
    @Override
    public <T extends ToolNumber> T divInvi(Number num) {
        updateThis(divTool(convert(num), this, (C) this));
        return (T) this;
    }
    
    /* 
     * Calculate the division of tn1 and tn2.
     * Modifies and returns {@code result}.
     * 
     * @param tn1
     * @param tn2
     * @result will contain the result of the operation. If null, a new
     *     ToolNumber should be created.
     * @return if {@code result == null}, {@code tn1 / tn2}.
     *     Otherwise {@code result}, where {@code result == tn1 / tn2}.
     */
    protected abstract <V extends C> V divTool(ToolNumber tn1, ToolNumber tn2,
                                               V result);
    
    
    /**-------------------------------------------------------------------------
     * Modulo.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T mod(Number num) {
        return (T) modTool(this, convert(num), null);
    }
    
    @Override
    public <T extends ToolNumber> T modi(Number num) {
        T result = (T) modTool(this, convert(num), (C) this);
        updateThis(result);
        return (T) this;
    }
    
    @Override
    public <T extends ToolNumber> T modInv(Number num) {
        return (T) modTool(convert(num), this, null);
    }
    
    @Override
    public <T extends ToolNumber> T modInvi(Number num) {
        T result = (T) modTool(convert(num), this, (C) this);
        updateThis(result);
        return (T) this;
    }
    
    /**
     * Calculate tn1 modulo tn2.
     * Modifies and returns {@code result}.
     * 
     * @param tn1
     * @param tn2
     * @result will contain the result of the operation. If null, a new
     *     ToolNumber should be created.
     * @return if {@code result == null}, {@code tn1 % tn2}.
     *     Otherwise {@code result}, where {@code result == tn1 % tn2}.
     */
    protected abstract <V extends C> V modTool(ToolNumber tn1, ToolNumber tn2,
                                               V result);
    
    
}