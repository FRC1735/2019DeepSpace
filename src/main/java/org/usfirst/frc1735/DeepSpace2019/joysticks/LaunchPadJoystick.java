/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.joysticks;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienAttackLight;
import org.usfirst.frc1735.DeepSpace2019.commands.ClawCmd;
import org.usfirst.frc1735.DeepSpace2019.commands.OrangeLight;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Claw;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * Add your docs here.
 */
public class LaunchPadJoystick extends AbstractJoystick {

    public LaunchPadJoystick(final Joystick joystick, final Role role) {
        super(joystick, role);
    }

    @Override
    void initializeKeymap() {
        // TODO - BUTTON IDs are NOT CORRECT

        // since the LaunchPad can only do a specific set of things,
        // we do not switch for ROLE here, the LaunchPad only has one role

        Robot.oi.launchPadOne = new JoystickButton(joystick, 1);
        // WhileHeld seems to cause stuttering (perhaps new behavior in 2019?  It starts and stops and stutters.  perhaps terminating the command and stopping the motor every iteration?)
        // Workaround:  Explicit commands on press and release to accomplish the same thing
        Robot.oi.launchPadOne.whenPressed(new ClawCmd(Claw.in));
        Robot.oi.launchPadOne.whenReleased(new ClawCmd(0));
      
        Robot.oi.launchPadTwo = new JoystickButton(joystick, 2);
        Robot.oi.launchPadTwo.whenPressed(new ClawCmd(Claw.out));
        Robot.oi.launchPadTwo.whenReleased(new ClawCmd(0));
    }

    @Override
    public double getX() {
        return joystick.getX();
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getSwivelStickX() {
        return 0;
    }

    @Override
    public double getSwivelStickY() {
        return 0;
    }

    @Override
    public boolean isCapableOfSoloTankMode() {
        return false;
    }
}
