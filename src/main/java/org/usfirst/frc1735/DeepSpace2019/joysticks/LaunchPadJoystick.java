/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.joysticks;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienAttackLight;
import org.usfirst.frc1735.DeepSpace2019.commands.OrangeLight;
import org.usfirst.frc1735.DeepSpace2019.subsystems.AlienDeployer;

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
        Robot.oi.launchPadZero = new JoystickButton(joystick, 0);
        Robot.oi.launchPadZero.whenPressed(new OrangeLight()); // TODO

        Robot.oi.launchPadOne = new JoystickButton(joystick, 1);
        Robot.oi.launchPadOne.whenPressed(new AlienAttackLight()); // TODO

        Robot.oi.launchPadTwo = new JoystickButton(joystick, 2);
        Robot.oi.launchPadTwo.whenPressed(new OrangeLight()); // TODO
    }

    @Override
    public double getX() {
        return 0;
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
