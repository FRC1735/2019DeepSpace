/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.commands;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.joysticks.AbstractJoystick;
import org.usfirst.frc1735.DeepSpace2019.joysticks.JoystickFactory;
import org.usfirst.frc1735.DeepSpace2019.joysticks.Role;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ArmWithRotarySwitch extends Command {
  public ArmWithRotarySwitch() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.arm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    m_lastKnownRotaryValue = 0;
    joystickFactory = new JoystickFactory();
    joystickRight = joystickFactory.get(Robot.oi.joyRight, Role.DRIVER_RIGHT);

    m_controllerTab = Shuffleboard.getTab("LaunchPad");
    m_dialValue = m_controllerTab.add("LaunchPad Dial", 0)
      .withSize(2, 1) // make the widget 2x1
      .withPosition(0, 0) // place it in the top-left corner
      .getEntry();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // TODO - delete logs
    final double rotaryValue = joystickRight.getX();
    if (rotaryValue != m_lastKnownRotaryValue) { // TODO - probably need to compare a range
      
    }        
    m_dialValue.setDouble(rotaryValue);
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

  private double m_lastKnownRotaryValue;
  JoystickFactory joystickFactory;  
  AbstractJoystick joystickRight;        
    // Pointer to the Shuffleboard tab for the Arm:
    ShuffleboardTab m_controllerTab;
    NetworkTableEntry m_dialValue;


}
