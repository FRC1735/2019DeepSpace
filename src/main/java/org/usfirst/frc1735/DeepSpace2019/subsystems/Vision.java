/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision extends Subsystem {

  @Override
  public void initDefaultCommand() {
    // TODO - is there a default command here?
  }

  @Override
  public void periodic() {
    // TODO
  }

  public void onRobotReady() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    tv = table.getEntry("tv"); // valid bit (target is detected)        
    tx = table.getEntry("tx"); // Horizontal angle to target in degrees
    ty = table.getEntry("ty"); // vertical angle to target in degrees
    ta = table.getEntry("ta"); // Area of target relative to full screen image in percent
    tx0 = table.getEntry("tx0"); // one of three raw Tx values.
    tx1 = table.getEntry("tx1"); // one of three raw Tx values.
    tx2 = table.getEntry("tx2"); // one of three raw Tx values.
    
    ShuffleboardTab limelightTab = Shuffleboard.getTab("Limelight");
  }

  public double getTv() {
    return tv.getDouble(0); // Valid bit
  }

  public double getTx() {
    return tx.getDouble(0);
  }
  
  public double getTx0() {
    return tx0.getDouble(0);
  }
  
  public double getTx1() {
    return tx1.getDouble(0);
  }
  
  public double getTx2() {
    return tx2.getDouble(0);
  }

  public double getTy() {
  return ty.getDouble(0);
  } 

  NetworkTableEntry tv, tx, tx0, tx1, tx2, ty, ta;
}
