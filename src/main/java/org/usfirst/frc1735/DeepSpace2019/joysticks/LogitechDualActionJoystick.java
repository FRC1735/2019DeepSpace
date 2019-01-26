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
    public double getX() {
        return joystick.getRawAxis(2);
    }

    @Override
    public double getY() {
        return joystick.getRawAxis(3);
    }   
    
    @Override
    void initializeKeymap() {
        switch(role) {
            case DRIVER_LEFT:
                Robot.oi.arcadeMode = new JoystickButton(joystick, 4);
                Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());

                Robot.oi.tankMode = new JoystickButton(joystick, 3);
                Robot.oi.tankMode.whenPressed(new EnterTankMode());
                break;

            case OPERATOR: 
                // TODO
                break;
        }
    }
}