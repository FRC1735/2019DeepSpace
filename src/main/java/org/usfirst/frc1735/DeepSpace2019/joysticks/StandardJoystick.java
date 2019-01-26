package org.usfirst.frc1735.DeepSpace2019.joysticks;


import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

// TODO - get the real name for this type of Joystick
public class StandardJoystick extends AbstractJoystick {

    public StandardJoystick(final Joystick joystick, final Role role) {
        super(joystick, role);
    }

    @Override
    void initializeKeymap() {
        switch (role) {
            case DRIVER_LEFT:
                Robot.oi.arcadeMode = new JoystickButton(joystick, 2);
                Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());
                
                Robot.oi.tankMode = new JoystickButton(joystick, 1);
                Robot.oi.tankMode.whenPressed(new EnterTankMode());
                break;  

            case OPERATOR:
                // TODO
                break;
        }
    }

    @Override
    public double getX() {
        return joystick.getX();
    }

    @Override
    public double getY() {
        return joystick.getY();
    }

}