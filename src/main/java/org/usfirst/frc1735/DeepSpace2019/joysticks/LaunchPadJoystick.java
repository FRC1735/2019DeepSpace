/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.joysticks;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienAttackLight;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienExtendedClosed;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienExtendedOpen;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienRetractedClosed;
import org.usfirst.frc1735.DeepSpace2019.commands.ClawCmd;
import org.usfirst.frc1735.DeepSpace2019.commands.OrangeLight;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Claw;
import org.usfirst.frc1735.DeepSpace2019.subsystems.HatchGrabber;
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

        Robot.oi.launchPadOne = new JoystickButton(joystick, 1);
        // WhileHeld seems to cause stuttering (perhaps new behavior in 2019?  It starts and stops and stutters.  perhaps terminating the command and stopping the motor every iteration?)
        // Workaround:  Explicit commands on press and release to accomplish the same thing
        Robot.oi.launchPadOne.whenPressed(new ClawCmd(Claw.in));
        Robot.oi.launchPadOne.whenReleased(new ClawCmd(0));
      
        Robot.oi.launchPadTwo = new JoystickButton(joystick, 2);
        Robot.oi.launchPadTwo.whenPressed(new ClawCmd(Claw.out));
        Robot.oi.launchPadTwo.whenReleased(new ClawCmd(0));

        Robot.oi.launchPadRetractedClosed = new JoystickButton(joystick, 3);
        Robot.oi.launchPadRetractedClosed.whenPressed(new AlienRetractedClosed());

        Robot.oi.launchPadExtendedClosed = new JoystickButton(joystick, 4);
        Robot.oi.launchPadExtendedClosed.whenPressed(new AlienExtendedClosed());

        Robot.oi.launchPadExtendedOpen = new JoystickButton(joystick, 5);
        Robot.oi.launchPadExtendedOpen.whenPressed(new AlienExtendedOpen());
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

    //Launchpad has the ability to drive outputs (which we hook to the button LEDs)
    public void setAlienRetractedClosedLED(boolean newState) {
        joystick.setOutput(1, newState);
    }
    public void setAlienExtendedClosedLED(boolean newState) {
        joystick.setOutput(2, newState);
    }
    public void setAlienExtendedOpenLED(boolean newState) {
        joystick.setOutput(3, newState);
    }

    // This function looks at the hardware state to determine what state the alien is in, and set button lights accordingly
    public void updateAlienLightState() {
        if ((Robot.alienDeployer.getState() == AlienDeployer.State.RETRACTED)
            && (Robot.hatchGrabber.getState() == HatchGrabber.State.CLOSED)) {
                setAlienRetractedClosedLED(true);
                setAlienExtendedClosedLED(false);
                setAlienExtendedOpenLED(false);
            }
            else if ((Robot.alienDeployer.getState() == AlienDeployer.State.EXTENDED)
            && (Robot.hatchGrabber.getState() == HatchGrabber.State.CLOSED)) {
                setAlienRetractedClosedLED(false);
                setAlienExtendedClosedLED(true);
                setAlienExtendedOpenLED(false);
            }
            else if ((Robot.alienDeployer.getState() == AlienDeployer.State.EXTENDED)
            && (Robot.hatchGrabber.getState() == HatchGrabber.State.OPENED)) {
                setAlienRetractedClosedLED(false);
                setAlienExtendedClosedLED(false);
                setAlienExtendedOpenLED(true);
            }
            else {
                // None of the above, so for now just turn the lights all off.
                // TODO:  perhaps blink if a subsystem is in motion, or if a command is in the process of being executed?
                setAlienRetractedClosedLED(false);
                setAlienExtendedClosedLED(false);
                setAlienExtendedOpenLED(false);
                 
            }

    }
}
