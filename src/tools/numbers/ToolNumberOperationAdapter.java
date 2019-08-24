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

/**
 * TODO
 * 
 * @version 0.0
 * @author Kaj Wortel
 * 
 * @deprecated Old class, and needs to be refactored.
 */
@Deprecated
public abstract class ToolNumberOperationAdapter 
        extends ToolNumber {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Does a given operation on two numbers.
     * 
     * @param num1 the first number of the operation.
     * @param num2 the second number of the operation.
     * @param op the operation to be performed.
     * @param update whether to update the current state.
     * @return the result of the operation.
     * @throws NullPointerException iff
     *     {@code num1} or {@code num2} are {@code null}.
     * 
     * Note that either {@code num1} OR {@code num2} will be {@code this}.
     */
    protected abstract <T extends ToolNumber> T doOperation
            (Number num1, Number num2, Operation op, boolean update);
    
    
    /* -------------------------------------------------------------------------
     * Overrides from ToolNumber.
     * -------------------------------------------------------------------------
     */
    /* -------------------------------------------------------------------------
     * Addition.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T add(Number num) {
        return doOperation(getValue(), num, Operation.ADD, false);
    }
    
    @Override
    public <T extends ToolNumber> T addi(Number num) {
        return doOperation(getValue(), num, Operation.ADD, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Inverse addition.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T addInv(Number num) {
        return doOperation(num, getValue(), Operation.ADD, false);
    }
    
    @Override
    public <T extends ToolNumber> T addInvi(Number num) {
        return doOperation(num, getValue(), Operation.SUB, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Subtraction.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T sub(Number num) {
        return doOperation(getValue(), num, Operation.SUB, false);
    }
    
    @Override
    public <T extends ToolNumber> T subi(Number num) {
        return doOperation(getValue(), num, Operation.SUB, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Inverse subtraction.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T subInv(Number num) {
        return doOperation(num, getValue(), Operation.SUB, false);
    }
    
    @Override
    public <T extends ToolNumber> T subInvi(Number num) {
        return doOperation(num, getValue(), Operation.SUB, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Multiplication.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T mul(Number num) {
        return doOperation(getValue(), num, Operation.MUL, false);
    }
    
    @Override
    public <T extends ToolNumber> T muli(Number num) {
        return doOperation(getValue(), num, Operation.MUL, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Inverse multiplication.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T mulInv(Number num) {
        return doOperation(num, getValue(), Operation.MUL, false);
    }
    
    @Override
    public <T extends ToolNumber> T mulInvi(Number num) {
        return doOperation(num, getValue(), Operation.MUL, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Division.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T div(Number num) {
        return doOperation(getValue(), num, Operation.DIV, false);
    }
    
    @Override
    public <T extends ToolNumber> T divi(Number num) {
        return doOperation(getValue(), num, Operation.DIV, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Inverse division.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T divInv(Number num) {
        return doOperation(num, getValue(), Operation.DIV, false);
    }
    
    @Override
    public <T extends ToolNumber> T divInvi(Number num) {
        return doOperation(num, getValue(), Operation.DIV, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Modulo.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T mod(Number num) {
        return doOperation(getValue(), num, Operation.MOD, false);
    }
    
    @Override
    public <T extends ToolNumber> T modi(Number num) {
        return doOperation(getValue(), num, Operation.MOD, true);
    }
    
    
    /* -------------------------------------------------------------------------
     * Inverse modulo.
     * -------------------------------------------------------------------------
     */
    @Override
    public <T extends ToolNumber> T modInv(Number num) {
        return doOperation(num, getValue(), Operation.MOD, false);
    }
    
    @Override
    public <T extends ToolNumber> T modInvi(Number num) {
        return doOperation(num, getValue(), Operation.MOD, true);
    }
    
    
}