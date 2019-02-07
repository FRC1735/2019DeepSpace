package org.usfirst.frc1735.DeepSpace2019.joysticks;


import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.ClawCmd;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Claw;

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
                Robot.oi.arcadeMode = new JoystickButton(joystick, 11);
                Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());
                
                Robot.oi.tankMode = new JoystickButton(joystick, 10);
                Robot.oi.tankMode.whenPressed(new EnterTankMode());

                Robot.oi.clawIn = new JoystickButton(joystick, 2);
                Robot.oi.clawIn.whileHeld(new ClawCmd(Claw.in));

                Robot.oi.clawOut = new JoystickButton(joystick, 1);
                Robot.oi.clawOut.whileHeld(new ClawCmd(Claw.out));
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
    public double getSwivelStickX() {
        return 0;
    }

    @Override
    public double getSwivelStickY() {
        return 0;
    }
}