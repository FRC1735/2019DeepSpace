/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.commands;

import java.util.Map;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.joysticks.AbstractJoystick;
import org.usfirst.frc1735.DeepSpace2019.joysticks.JoystickFactory;
import org.usfirst.frc1735.DeepSpace2019.joysticks.Role;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Arm;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ArmWithRotarySwitch extends Command {
  public ArmWithRotarySwitch() {
    requires(Robot.arm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    joystickFactory = new JoystickFactory();
    joystickRight = joystickFactory.get(Robot.oi.joyRight, Role.DRIVER_RIGHT);
    // populate array to hold the mapping from switch index to degrees

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    final double rotaryValue = joystickRight.getX();
    // Floating values seem to end up around zero. Ignore those, as they are
    // "Between" switch positions...
    if (rotaryValue == 0) {
    } else if (rotaryValue < 0) {
      // if the value is -1 (or negative in general), then we want to override and use
      // the Operatior Joystick
      System.out.println("Operator Joystick Override");
      Robot.arm.simpleMoveArm(-Robot.oi.operator.getY()); // y axis fwd is negative
    } else { // This is a valid PID setpoint
      // Map the rotary dial values to degrees and into setpoints and move the arm to
      // the right place
      double setpointDegrees = this.rotary2Degrees(rotaryValue); // What is the angle we're requestin?
      double setpointTicks = Robot.arm.degreesToTicks(setpointDegrees); // Map that to ticks; takes into account any
                                                                        // taring
      System.out.println("PID to " + setpointDegrees + "Degrees");

      Robot.arm.PIDMoveArm(setpointTicks);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  double rotary2Degrees(double rotaryValue) {
    // Step 1: find which preset from the rotary value.
    // Magic equation: There is an average of 0.9055 between each of the 11
    // positions.
    // therefore: round (rotary_value/0.09055)
    int index = (int) Math.round(rotaryValue / 0.09055);
    return indexDegreeMap[index - 1];
  }

  JoystickFactory joystickFactory;
  AbstractJoystick joystickRight;

  // Array variable for mapping switch index to angle
  // Starts from back of robot and goes forward

  double[] indexDegreeMap = new double[] { Arm.kBackwardBallPickup, Arm.kBackwardHatchPickup, Arm.kBackwardRocketOne,
      Arm.kBackwardCargo, Arm.kBackwardRocketTwo, Arm.kUp, Arm.kForwardRocketTwo, Arm.kForwardCargo,
      Arm.kForwardRocketOne, Arm.kForwardHatchPickup, Arm.kForwardBallPickup };
}
