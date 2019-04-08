package org.usfirst.frc1735.DeepSpace2019.joysticks;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.ClawCmd;
import org.usfirst.frc1735.DeepSpace2019.commands.ClimbExtend;
import org.usfirst.frc1735.DeepSpace2019.commands.ClimbRetract;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Claw;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XBoxJoystick extends AbstractJoystick {
    private static final int BUTTON_X = 3;
    private static final int BUTTON_Y = 4;

    // 0 and 1 are left xbox controller swivel stick

    public XBoxJoystick(final Joystick joystick, final Role role) {
        super(joystick, role);
    }

    @Override
    public double getX() {
        return joystick.getRawAxis(0);
    }

    @Override
    public double getY() {
        return joystick.getRawAxis(1);
    }

    @Override
    public double getSwivelStickX() {
        return joystick.getRawAxis(4);
    }

    @Override
    public double getSwivelStickY() {
        return joystick.getRawAxis(5);
    }

    @Override
    void initializeKeymap() {
        switch (role) {
            case DRIVER_LEFT:
                Robot.oi.arcadeMode = new JoystickButton(joystick, 7);
                Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());

                Robot.oi.tankMode = new JoystickButton(joystick, 8);
                Robot.oi.tankMode.whenPressed(new EnterTankMode());

                Robot.oi.clawIn = new JoystickButton(joystick, 1);
                //Robot.oi.clawIn.whileHeld(new ClawCmd(Claw.in));
                // WhileHeld seems to cause stuttering (perhaps new behavior in 2019?  It starts and stops and stutters.  perhaps terminating the command and stopping the motor every iteration?)
                // Workaround:  Explicit commands on press and release to accomplish the same thing
                Robot.oi.clawIn.whenPressed(new ClawCmd(Claw.in));
                Robot.oi.clawIn.whenReleased(new ClawCmd(0));
              
                Robot.oi.clawOut = new JoystickButton(joystick, 2);
//                Robot.oi.clawOut.whileHeld(new ClawCmd(Claw.out));
                Robot.oi.clawOut.whenPressed(new ClawCmd(Claw.out));
                Robot.oi.clawOut.whenReleased(new ClawCmd(0));

                Robot.oi.extendClimber = new JoystickButton(joystick, 3);
                Robot.oi.extendClimber.whenPressed(new ClimbExtend());

                Robot.oi.retractClimber = new JoystickButton(joystick, 4);
                Robot.oi.retractClimber.whenPressed(new ClimbRetract());

                break;

            case DRIVER_RIGHT:
            case OPERATOR:
                // Do nothing for now
                break;
        }
    }

    @Override
    public boolean isCapableOfSoloTankMode() {
        return true;
    }
}
