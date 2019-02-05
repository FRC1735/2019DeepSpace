package org.usfirst.frc1735.DeepSpace2019.joysticks;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class LogitechDualActionJoystick extends AbstractJoystick {

    public LogitechDualActionJoystick(final Joystick joystick, final Role role) {
        super(joystick, role);
    }

    @Override
    void initializeKeymap() {
        switch(role) {
            case DRIVER_LEFT:
                Robot.oi.arcadeMode = new JoystickButton(joystick, 1);
                Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());

                Robot.oi.tankMode = new JoystickButton(joystick, 4);
                Robot.oi.tankMode.whenPressed(new EnterTankMode());

                Robot.oi.clawIn = new JoystickButton(joystick, 2);
                Robot.oi.clawIn.whenHeld(new ClawCmd(Claw.in));

                Robot.oi.clawOut = new JoystickButton(joystick, 3);
                Robot.oi.clawOut.whenHeld(new ClawCmd(Claw.out));
                break;

            case DRIVER_RIGHT:
            case OPERATOR: 
                // TODO
                break;
        }
    }

    @Override
    public boolean isCapableOfSoloTankMode() {
        return true;
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
        return joystick.getRawAxis(2);
    }

    @Override
    public double getSwivelStickY() {
        return joystick.getRawAxis(3);
    }

    

}