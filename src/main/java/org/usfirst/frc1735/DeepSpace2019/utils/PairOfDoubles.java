/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.utils;

/**
 * This class does not allow you to modify the pair of values once constructed.
 */
public class PairOfDoubles {

    public PairOfDoubles(double lval, double rval) {
        this.left = lval;
        this.right = rval;
    }

    private final double left;
    private final double right;
    
    public double getLeft() {
        return this.left;
    }
    public double getRight() {
        return this.right;
    }
}
