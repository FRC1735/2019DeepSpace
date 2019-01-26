package org.usfirst.frc1735.DeepSpace2019.joysticks;


import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Attack3Joystick extends AbstractJoystick {

    public Attack3Joystick(final Joystick joystick, final Role role) {
        super(joystick, role);
    }

    @Override
    public double getX() {
        return joystick.getX();
    }

    @Override
    public double getY() {
        return joystick.getY();
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
           
            case DRIVER_RIGHT:
            case OPERATOR:
                // TODO
                break;
        }
    }

    @Override
    public boolean isCapableOfSoloTankMode() {
        return false;
    }

    @Override
    public double getXSwivelStick() {
        return 0;
    }

    @Override
    public double getYSwivelStick() {
        return 0;
    }
}