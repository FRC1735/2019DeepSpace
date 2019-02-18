/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.commands;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Vision.CameraMode;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class SetCameraDrivingMode extends InstantCommand {

  public SetCameraDrivingMode() {
    setRunWhenDisabled(true);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.vision.setCameraMode(CameraMode.DRIVING);
  }

}
